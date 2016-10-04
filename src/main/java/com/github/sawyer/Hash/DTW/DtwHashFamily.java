package com.github.sawyer.Hash.DTW;

import com.github.sawyer.Hash.DistanceMeasure;
import com.github.sawyer.Hash.HashFamily;
import com.github.sawyer.Hash.HashFunction;

import java.util.Arrays;

/**
 * @author sunyue
 * @version 1.0    2016/10/1 12:27
 */
public class DtwHashFamily implements HashFamily {
    private int w;
    private int numOfPoints;

    public DtwHashFamily(int w) {
        this.w = w;
        this.numOfPoints = 5599;     // 写死max=5562
    }

    @Override
    public HashFunction createHashFunction() {
        return new DtwHash(w, numOfPoints);
    }

    @Override
    public Integer combine(int[] hashes) {
        return Arrays.hashCode(hashes);
    }

    @Override
    public DistanceMeasure createDistanceMeasure() {
        return new DtwDistance();
    }

    @Override
    public String toString() {
        return "DtwHashFamily";
    }
}
