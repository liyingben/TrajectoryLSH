package com.github.sawyer.Core;

import com.github.sawyer.Hash.DistanceMeasure;
import com.github.sawyer.Hash.HashFamily;
import com.github.sawyer.Util.FileUtils;
import com.github.sawyer.Util.SearchUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author sunyue
 * @version 1.0    2016/9/27 14:41
 */
public class LSH {

    private final HashFamily hashFamily;
    private List<Trajectory> dataSet;
    private Index index;


    public LSH(HashFamily hashFamily, List<Trajectory> dataSet) {
        this.hashFamily = hashFamily;
        this.dataSet = dataSet;
    }

    public static void main(String[] args) {

        CommandLineInterface cli = new CommandLineInterface(args);
        cli.parseArguments();
        cli.startApplication();
    }

    /**
     * Read data into dataSet.
     *
     * @param fileName the filename, an exception thrown if the file does not exist
     * @param maxSize  The maximum number of trajectories to read.
     *                 Integer.MAX_VALUE for reading all trajectories.
     * @return dataSet
     */
    public static List<Trajectory> readData(String fileName, int maxSize) {
        return FileUtils.readData(fileName, "\t", maxSize);
    }

    /**
     * construct the index
     *
     * @param numOfHashes     number of hash functions
     * @param numOfHashTables number of hash tables
     */
    public void buildIndex(int numOfHashes, int numOfHashTables) {
        long startTime = System.currentTimeMillis();
        index = new Index(hashFamily, numOfHashes, numOfHashTables);
        if (dataSet != null) {
            for (Trajectory trajectory : dataSet) {
                index.index(trajectory);
            }
        }
        double time = (System.currentTimeMillis() - startTime) / 1000.0;
//        System.out.println("Build Index time :" + time);
    }


    public List<Trajectory> linearSearch(List<Trajectory> dataSet, final Trajectory query, int k, DistanceMeasure measure) {
        return SearchUtils.linearSearch(dataSet, query, k, measure);
    }

    /**
     * start benchmark test between linear Search and LSH Search.
     *
     * @param queries queries dataSet
     * @param k       number of neighbours
     * @param measure distance measure
     */
    public void benchmark(List<Trajectory> queries, int k, DistanceMeasure measure) {
        long startTime;
        double lineSearchTime = 0;
        double lshSearchTime = 0;
        int numberCorrect = 0;

        int falsePositives = 0;     // 误报
        int truePositives = 0;      // 正报
        int falseNegatives = 0;     // 漏报

        for (int i = 0; i < queries.size(); i++) {
            Trajectory query = queries.get(i);

            startTime = System.currentTimeMillis();
            List<Trajectory> lshResult = index.query(query, k);
            lshSearchTime += System.currentTimeMillis() - startTime;

            startTime = System.currentTimeMillis();
            List<Trajectory> linearResult = linearSearch(dataSet, query, k, measure);
            lineSearchTime += System.currentTimeMillis() - startTime;

            Set<Trajectory> set = new HashSet<>();  // union set
            set.addAll(lshResult);
            set.addAll(linearResult);

            falsePositives += set.size() - linearResult.size();
            truePositives += lshResult.size() + linearResult.size() - set.size();
            falseNegatives += set.size() - lshResult.size();

            boolean correct = true;
            for (int j = 0; j < Math.min(lshResult.size(), linearResult.size()); j++) {
                correct = correct && lshResult.get(j) == linearResult.get(j);
            }
            if (correct) {
                numberCorrect++;
            }
        }

        double queriesSize = queries.size();
        double dataSetSize = dataSet.size();
        double precision = truePositives / (double) (truePositives + falsePositives) * 100;
        double recall = truePositives / (double) (truePositives + falseNegatives) * 100;
        double percentageCorrect = numberCorrect / queriesSize * 100;
        double percentageTouched = index.getTouched() / queriesSize / dataSetSize * 100;
        lineSearchTime /= 1000.0;
        lshSearchTime /= 1000.0;
        int numOfHashes = index.getNumOfHashes();
        int numOfHashTables = index.getNumOfHashTables();
//        System.out.println("-------------------------------------------------------------------------------");
//        System.out.println("queriesSize: " + queriesSize);
//        System.out.println("dataSetSize:" + dataSetSize);
//        System.out.println("truePositives:" + truePositives);
//        System.out.println("falsePositives:" + falsePositives);
//        System.out.println("falseNegatives:" + falseNegatives);
//        System.out.println("numberCorrect:" + numberCorrect);
//        System.out.println("getTouched:" + index.getTouched());

        System.out.printf("%12d %15d %15d %12.2f %12.2f %12.4f %12.4f %12.2f %12.2f\n", numOfHashes, numOfHashTables, k, percentageCorrect, percentageTouched, lineSearchTime, lshSearchTime, precision, recall);
    }

    /**
     * Find the k nearest neighbours for a query trajectory in the index.
     *
     * @param query the query trajectory
     * @param k     The size of the neighbourhood
     * @return A list of nearest neighbours, with the length between 0 to k.
     */
    public List<Trajectory> query(final Trajectory query, int k) {
        return index.query(query, k);
    }
}
