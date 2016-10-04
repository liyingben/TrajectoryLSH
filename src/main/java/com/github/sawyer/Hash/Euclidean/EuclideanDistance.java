package com.github.sawyer.Hash.Euclidean;

import com.github.sawyer.Core.Trajectory;
import com.github.sawyer.Hash.DistanceMeasure;

/**
 * @author sunyue
 * @version 1.0    2016/9/28 13:24
 */
public class EuclideanDistance implements DistanceMeasure {
    @Override
    public double distance(Trajectory one, Trajectory other) {
        double distance = 0.0;
        int len = one.length() < other.length() ? one.length() : other.length();
        for (int i = 0; i < len; i++) {
            distance += one.getPoint(i).euclidean(other.getPoint(i));
        }
        return distance;
    }
}
