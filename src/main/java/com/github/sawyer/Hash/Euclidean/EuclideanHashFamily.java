package com.github.sawyer.Hash.Euclidean;

import com.github.sawyer.Hash.DistanceMeasure;
import com.github.sawyer.Hash.HashFamily;
import com.github.sawyer.Hash.HashFunction;

import java.util.Arrays;

/**
 * @author sunyue
 * @version 1.0    2016/9/28 14:00
 */
public class EuclideanHashFamily implements HashFamily {
    private int w;
    private int numOfPoints;

    public EuclideanHashFamily(int w) {
        this.w = w;
        this.numOfPoints = 5599;     // 写死max=5562
    }

    @Override
    public HashFunction createHashFunction() {
        return new EuclideanHash(w, numOfPoints);
    }

    @Override
    public Integer combine(int[] hashes) {
        return Arrays.hashCode(hashes);
    }

    @Override
    public DistanceMeasure createDistanceMeasure() {
        return new EuclideanDistance();
    }

    @Override
    public String toString() {
        return "EuclideanHashFamily";
    }
}
