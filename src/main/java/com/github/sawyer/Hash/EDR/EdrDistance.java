package com.github.sawyer.Hash.EDR;

import com.github.sawyer.Core.Point;
import com.github.sawyer.Core.Trajectory;
import com.github.sawyer.Hash.DistanceMeasure;

/**
 * @author sunyue
 * @version 1.0    2016/10/2 12:02
 */
public class EdrDistance implements DistanceMeasure {
    private double threshHold = 0.0001;

    @Override
    public double distance(Trajectory one, Trajectory other) {
        double[][] matrix = new double[one.length()][other.length()];
        matrix[0][0] = isMatch(one.getPoint(0), other.getPoint(0)) ? 0 : 1;
        for (int i = 1; i < one.length(); i++) {
            matrix[i][0] = matrix[i - 1][0] + 1;
        }
        for (int j = 1; j < other.length(); j++) {
            matrix[0][j] = matrix[0][j - 1] + 1;
        }
        for (int i = 1; i < one.length(); i++) {
            for (int j = 1; j < other.length(); j++) {
                double subCost = isMatch(one.getPoint(i - 1), other.getPoint(j - 1)) ? 0 : 1;
                matrix[i][j] = min3(matrix[i - 1][j - 1] + subCost, matrix[i - 1][j] + 1, matrix[i][j - 1]) + 1;
            }
        }
        return matrix[one.length() - 1][other.length() - 1];
    }

    private boolean isMatch(Point a, Point b) {
        return Math.abs(a.getLongitude() - b.getLongitude()) <= threshHold && Math.abs(a.getLatitude() - b.getLatitude()) <= threshHold;
    }

    private double min3(double a, double b, double c) {
        return Math.min(Math.min(a, b), c);
    }
}
