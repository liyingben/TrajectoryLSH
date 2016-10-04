package com.github.sawyer.Util;

import com.github.sawyer.Core.Trajectory;
import com.github.sawyer.Hash.DistanceComparator;
import com.github.sawyer.Hash.DistanceMeasure;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author sunyue
 * @version 1.0    2016/9/30 15:37
 */
public class SearchUtils {

    /**
     * linear Search the k nearest neighbours in the dataSet.
     *
     * @param dataSet dataSet
     * @param query   the tarjectory to query
     * @param k       number of neighbours
     * @param measure distance measure
     * @return top k neighbours
     */
    public static List<Trajectory> linearSearch(List<Trajectory> dataSet, Trajectory query, int k, DistanceMeasure measure) {
        List<Trajectory> trajs = new ArrayList<>();
        if (dataSet.size() <= k) {
            return trajs;
        }

        DistanceComparator comparator = new DistanceComparator(query, measure);
        PriorityQueue<Trajectory> priorityQueue = new PriorityQueue<>(dataSet.size(), comparator);
        priorityQueue.addAll(dataSet);

        for (int i = 0; i < k; i++) {
            trajs.add(priorityQueue.poll());
        }
        return trajs;
    }
}
