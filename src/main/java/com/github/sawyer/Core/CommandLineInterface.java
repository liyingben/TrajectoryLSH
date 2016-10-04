package com.github.sawyer.Core;

import com.github.sawyer.Hash.DTW.DtwHashFamily;
import com.github.sawyer.Hash.EDR.EdrHashFamily;
import com.github.sawyer.Hash.Euclidean.EuclideanHashFamily;
import com.github.sawyer.Hash.Frechet.FrechetHashFamily;
import com.github.sawyer.Hash.HashFamily;

import java.util.List;

/**
 * @author sunyue
 * @version 1.0    2016/9/28 16:33
 */
public class CommandLineInterface {
    boolean benchmark = false;
    private String[] arguments;
    private int numOfHashTables;
    private int numOfHashes;
    private int numOfNeighbours;
    private double radius;
    private List<Trajectory> dataSet;
    private List<Trajectory> queries;

    private HashFamily hashFamily;

    public CommandLineInterface(String[] arguments) {
        this.arguments = arguments;
    }

    public void parseArguments() {
        benchmark = hasOption("-b");

        radius = getDoubleValue("-r", 0.0);
        numOfNeighbours = getIntegerValue("-n", 4);
        numOfHashTables = getIntegerValue("-t", 4);
        numOfHashes = getIntegerValue("-h", 4);

        String dataSetFile = getValue("-d", null);
        String queryFile = getValue("-q", null);
        if (dataSetFile == null) {
            System.err.println("no '-d' argument find");
            System.exit(0);
        }
        if (queryFile == null) {
            System.err.println("no '-q' argument find");
            System.exit(0);
        }
        dataSet = LSH.readData(dataSetFile, 1000);
        queries = LSH.readData(queryFile, Integer.MAX_VALUE);

        String hashFamilyType = getValue("-f", "fre");
        hashFamily = getHashFamily(hashFamilyType, radius);
    }

    public void startApplication() {
        if (benchmark) {
            startBenchmark();
        } else {
            startLSH();
        }
    }

    /**
     * start benchmark test
     */
    public void startBenchmark() {
        System.out.println("Starting TrajectoryLSH benchmark with <<" + dataSet.size() + ">> GPS trajectories with different Distance Measure");
        System.out.println("with different number of Hash Functions, different number of Hash Tables.");
        System.out.println("Queries size is <<" + queries.size() + ">>");
        printLine();

        int[] numOfHash = {2, 4, 8, 16};
        int[] numOfTable = {2, 4, 8};

        int w = (int) (10 * radius);
        w = w == 0 ? 1 : w;
        HashFamily[] families = {
                new EuclideanHashFamily(w),
                new DtwHashFamily(w),
                new FrechetHashFamily(w),
                new EdrHashFamily(w),
        };

        for (HashFamily family : families) {
            LSH lsh = new LSH(family, dataSet);
            System.out.println("Hash Family is <<" + family.toString() + ">>");
            System.out.printf("%12s %15s %15s %12s %12s %12s %12s %12s %12s\n", "#hashes", "#hashTables", "#neighbours", "Correct", "Touched", "Linear", "LSH", "Precision", "Recall");
            for (int aNumOfHash : numOfHash) {
                for (int aNumOfTable : numOfTable) {
                    lsh.buildIndex(aNumOfHash, aNumOfTable);
                    lsh.benchmark(queries, numOfNeighbours, family.createDistanceMeasure());
                }
            }
            printLine();
        }
    }

    /**
     * start test LSH with specific Hash Family
     */
    public void startLSH() {
        System.out.println("Starting <<" + hashFamily.toString() + ">> test with <<" + dataSet.size() + ">> GPS trajectories");
        System.out.println("with different number of Hash Functions, different number of Hash Tables.");
        System.out.println("Queries size is <<" + queries.size() + ">>");

        int[] numOfHash = {2, 4, 8, 16};
        int[] numOfTable = {2, 4, 8};
        LSH lsh = new LSH(hashFamily, dataSet);

        System.out.printf("%12s %15s %15s %12s %12s %12s %12s %12s %12s\n", "#hashes", "#hashTables", "#neighbours", "Correct", "Touched", "Linear", "LSH", "Precision", "Recall");

        for (int aNumOfHash : numOfHash) {
            for (int aNumOfTable : numOfTable) {
                lsh.buildIndex(aNumOfHash, aNumOfTable);
                lsh.benchmark(queries, numOfNeighbours, hashFamily.createDistanceMeasure());
            }
        }

    }

    private HashFamily getHashFamily(String hashFamilyType, double radius) {
        int w = (int) (10 * radius);
        w = w == 0 ? 1 : w;
        HashFamily family = null;
        if (hashFamilyType.equalsIgnoreCase("eu")) {
            family = new EuclideanHashFamily(w);
        } else if (hashFamilyType.equalsIgnoreCase("dtw")) {
            family = new DtwHashFamily(w);
        } else if (hashFamilyType.equalsIgnoreCase("fre")) {
            family = new FrechetHashFamily(w);
        } else if (hashFamilyType.equalsIgnoreCase("edr")) {
            family = new EdrHashFamily(w);
        }
        return family;
    }

    private Double getDoubleValue(String option, Double defaultValue) {
        String value = getValue(option, defaultValue.toString());
        Double doubleValue = null;
        try {
            doubleValue = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            String message;
            message = "Expected double argument for option " + option + ",  " + value + " is" +
                    " not an double.";
            printError(message);

        }
        return doubleValue;
    }

    private Integer getIntegerValue(String option, Integer defaultValue) {
        String value = getValue(option, defaultValue.toString());
        Integer integerValue = null;
        try {
            integerValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            String message;
            message = "Expected integer argument for option " + option + ",  " + value + " is" +
                    " not an integer.";
            printError(message);

        }
        return integerValue;
    }

    private String getValue(String option, String defaultValue) {
        int index = -1;
        final String value;
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i].equalsIgnoreCase(option)) {
                index = i;
            }
        }
        if (index >= 0) {
            value = arguments[index + 1];
        } else {
            value = defaultValue;
        }
        return value;
    }

    private boolean hasOption(String option) {
        int index = -1;
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i].equalsIgnoreCase(option)) {
                index = i;
            }
        }
        return index >= 0;
    }

    private void printLine() {
        System.out.println("----------------------------------------------------");
    }

    private void printError(String message) {
        printLine();
        System.out.println(message);

    }
}
