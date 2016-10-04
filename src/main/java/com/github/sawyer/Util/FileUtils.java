package com.github.sawyer.Util;

import com.github.sawyer.Core.Point;
import com.github.sawyer.Core.Trajectory;

import java.io.*;
import java.util.*;

/**
 * An utility class for file interaction.
 *
 * @author sunyue
 * @version 1.0    2016/9/27 14:40
 */
public class FileUtils {
    /**
     * Read data to dataSet
     *
     * @param fileName  the filename, an exception thrown if the file does not exist
     * @param separator the separator, e.g. "\t" or ","
     * @param maxSize   The maximum number of trajectories to read.
     *                  Integer.MAX_VALUE for reading all trajectories.
     * @return dataSet
     */
    public static List<Trajectory> readData(final String fileName, final String separator, int maxSize) {
        List<Trajectory> dataSet = new ArrayList<>();
        BufferedReader br;


        try {
            File file = new File(fileName);
            if (maxSize <= 0){
                throw new Exception("maxSize <= 0 !");
            }

            if (!file.exists()) {
                throw new FileNotFoundException("File " + fileName + " don' exist!");
            }

            br = new BufferedReader(new FileReader(file));
            int lineNum = 0;
            String inputLine = br.readLine();
            while (inputLine != null && lineNum < maxSize) {
                lineNum++;
                String[] row = inputLine.split(separator);
                Trajectory trajectory = readRow(row);
                dataSet.add(trajectory);
                inputLine = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("can not open " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("wrong maxSize !");
        }

        return dataSet;
    }

    public static Trajectory readRow(final String[] row) {
        String id = row[0];
        int len = Integer.parseInt(row[1]);
        Point[] points = new Point[len];
        for (int i = 0; i < len; i++) {
            points[i] = new Point(Long.valueOf(row[2 + i * 3]), Double.valueOf(row[3 + i * 3]), Double.valueOf(row[4 + i * 3]));

        }
        return new Trajectory(id, points);
    }

    @Deprecated
    public static void generateQuereyFile(final String filename, final String target, final String separator, final int num) {
        List<Trajectory> dataSet = readData(filename, separator, -1);
        Random random = new Random();
        Set<Integer> set = new HashSet<>();
        while (set.size() < num) {
            int index = random.nextInt(dataSet.size());
            if (!set.contains(index)) {
                set.add(index);
            }
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(target)));
            Iterator<Integer> iterator = set.iterator();
            while (iterator.hasNext()) {

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
