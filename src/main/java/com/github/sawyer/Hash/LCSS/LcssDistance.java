package com.github.sawyer.Hash.LCSS;

import com.github.sawyer.Core.Point;
import com.github.sawyer.Core.Trajectory;
import com.github.sawyer.Hash.DistanceMeasure;

/**
 * @author sunyue
 * @version 1.0    2016/10/4 14:32
 */
public class LcssDistance implements DistanceMeasure {
    private double threshHold = 0.0001;
    private int difference = 1000;

    @Override
    public double distance(Trajectory one, Trajectory other) {
        double[][] matrix = new double[one.length()][other.length()];
        matrix[0][0] = isMatch(one.getPoint(0), other.getPoint(0)) ? 1 : 0;
        for (int i = 1; i < one.length(); i++) {
            matrix[i][0] = matrix[i - 1][0];
        }
        for (int j = 1; j < other.length(); j++) {
            matrix[0][j] = matrix[0][j - 1];
        }
        for (int i = 1; i < one.length(); i++) {
            for (int j = 1; j < other.length(); j++) {
                if (isMatch(one.getPoint(i), other.getPoint(j)) && Math.abs(one.length() - other.length()) <= difference) {
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;
                } else {
                    matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i][j - 1]);
                }
            }
        }
        return matrix[one.length() - 1][other.length() - 1];
    }

    private boolean isMatch(Point a, Point b) {
        return Math.abs(a.getLongitude() - b.getLongitude()) <= threshHold && Math.abs(a.getLatitude() - b.getLatitude()) <= threshHold;
    }
}
