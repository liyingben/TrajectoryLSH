package com.github.sawyer.Hash.DTW;

import com.github.sawyer.Core.Trajectory;
import com.github.sawyer.Hash.DistanceMeasure;

/**
 * @author sunyue
 * @version 1.0    2016/9/30 16:01
 */
public class DtwDistance implements DistanceMeasure {
    @Override
    public double distance(Trajectory one, Trajectory other) {
        double[][] matrix = new double[one.length()][other.length()];
        matrix[0][0] = one.getPoint(0).euclidean(other.getPoint(0));
        for (int i = 1; i < one.length(); i++) {
            matrix[i][0] = one.getPoint(i).euclidean(other.getPoint(0)) + matrix[i - 1][0];
        }
        for (int j = 1; j < other.length(); j++) {
            matrix[0][j] = other.getPoint(j).euclidean(one.getPoint(0)) + matrix[0][j - 1];
        }
        for (int i = 1; i < one.length(); i++) {
            for (int j = 1; j < other.length(); j++) {
                double subCost = one.getPoint(i).euclidean(other.getPoint(j));
                matrix[i][j] = subCost + min3(matrix[i - 1][j - 1], matrix[i - 1][j], matrix[i][j - 1]);
            }
        }
        return matrix[one.length() - 1][other.length() - 1];
    }

    private double min3(double a, double b, double c) {
        return Math.min(Math.min(a, b), c);
    }
}
