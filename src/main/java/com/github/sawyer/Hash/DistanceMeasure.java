package com.github.sawyer.Hash;

import com.github.sawyer.Core.Trajectory;

/**
 * A distance measure defines how distance is calculated, measured as it were, between two trajectories.
 * Each hash family has a corresponding distance measure which is abstracted using this interface.
 *
 * @author sunyue
 * @version 1.0    2016/9/27 14:42
 */
public interface DistanceMeasure {
    /**
     * Calculate the distance between two trajectories
     *
     * @param one   the first trajectory
     * @param other the other trajectory
     * @return A value representing the distance between two trajectories.
     */
    double distance(Trajectory one, Trajectory other);
}
