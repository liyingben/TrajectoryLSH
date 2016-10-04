package com.github.sawyer.Hash;

import com.github.sawyer.Core.Trajectory;

import java.util.Comparator;

/**
 * This comparator can be used to sort candidate neighbours according to their
 * distance to a query vector. Either for linear search or to sort the LSH
 * candidates found in colliding hash bins.
 *
 * @author sunyue
 * @version 1.0    2016/9/27 16:04
 */
public class DistanceComparator implements Comparator<Trajectory> {

    private final Trajectory queryTraj;
    private final DistanceMeasure measure;

    public DistanceComparator(Trajectory queryTraj, DistanceMeasure measure) {
        this.queryTraj = queryTraj;
        this.measure = measure;
    }

    @Override
    public int compare(Trajectory one, Trajectory other) {
        Double oneDistance = measure.distance(queryTraj, one);
        Double otherDistance = measure.distance(queryTraj, other);
        return oneDistance.compareTo(otherDistance);
    }
}
