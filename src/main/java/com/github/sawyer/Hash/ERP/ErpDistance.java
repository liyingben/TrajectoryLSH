package com.github.sawyer.Hash.ERP;

import com.github.sawyer.Core.Point;
import com.github.sawyer.Core.Trajectory;
import com.github.sawyer.Hash.DistanceMeasure;

/**
 * @author sunyue
 * @version 1.0    2016/10/4 14:08
 */
public class ErpDistance implements DistanceMeasure {
    private double threshHold = 0.0001;
    private Point g;

    public ErpDistance() {
        g = new Point(0, 136.0, 40.0);
    }

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

                matrix[i][j] = min3(matrix[i - 1][j - 1] + one.getPoint(i).euclidean(other.getPoint(j)),
                        matrix[i - 1][j] + one.getPoint(i).euclidean(g),
                        matrix[i][j - 1]) + other.getPoint(j).euclidean(g);
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
