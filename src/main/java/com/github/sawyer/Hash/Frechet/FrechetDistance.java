package com.github.sawyer.Hash.Frechet;

import com.github.sawyer.Core.Trajectory;
import com.github.sawyer.Hash.DistanceMeasure;

/**
 * @author sunyue
 * @version 1.0    2016/10/1 22:16
 */
public class FrechetDistance implements DistanceMeasure {
    @Override
    public double distance(Trajectory one, Trajectory other) {
        double[][] Matrix = new double[one.length()][other.length()];
        Matrix[0][0] = one.getPoint(0).euclidean(other.getPoint(0));
        for (int i = 1; i < one.length(); i++) {
            Matrix[i][0] = Math.max(one.getPoint(i).euclidean(other.getPoint(0)), Matrix[i - 1][0]);
        }
        for (int j = 1; j < other.length(); j++) {
            Matrix[0][j] = Math.max(other.getPoint(j).euclidean(one.getPoint(0)), Matrix[0][j - 1]);
        }
        for (int i = 1; i < one.length(); i++) {
            for (int j = 1; j < other.length(); j++) {
                double subCost = one.getPoint(i).euclidean(other.getPoint(j));
                Matrix[i][j] = Math.max(subCost, min3(Matrix[i - 1][j - 1], Matrix[i - 1][j], Matrix[i][j - 1]));
            }
        }
        return Matrix[one.length() - 1][other.length() - 1];
    }

    private double min3(double a, double b, double c) {
        return Math.min(Math.min(a, b), c);
    }
}
