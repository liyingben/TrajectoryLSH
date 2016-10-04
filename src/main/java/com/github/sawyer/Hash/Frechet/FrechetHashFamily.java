package com.github.sawyer.Hash.Frechet;

import com.github.sawyer.Hash.DistanceMeasure;
import com.github.sawyer.Hash.HashFamily;
import com.github.sawyer.Hash.HashFunction;

import java.util.Arrays;

/**
 * @author sunyue
 * @version 1.0    2016/10/1 22:30
 */
public class FrechetHashFamily implements HashFamily {

    private int w;
    private int numOfPoints;

    public FrechetHashFamily(int w) {
        this.w = w;
        this.numOfPoints = 5599;
    }

    @Override
    public HashFunction createHashFunction() {
        return new FrechetHash(w, numOfPoints);
    }

    @Override
    public Integer combine(int[] hashes) {
        return Arrays.hashCode(hashes);
    }

    @Override
    public DistanceMeasure createDistanceMeasure() {
        return new FrechetDistance();
    }

    @Override
    public String toString() {
        return "FrechetHashFamily";
    }
}
