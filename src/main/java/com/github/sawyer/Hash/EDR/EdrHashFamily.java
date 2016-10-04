package com.github.sawyer.Hash.EDR;

import com.github.sawyer.Hash.DistanceMeasure;
import com.github.sawyer.Hash.HashFamily;
import com.github.sawyer.Hash.HashFunction;

import java.util.Arrays;

/**
 * @author sunyue
 * @version 1.0    2016/10/2 15:07
 */
public class EdrHashFamily implements HashFamily {
    private int w;
    private int numOfPoints;

    public EdrHashFamily(int w) {
        this.w = w;
        this.numOfPoints = 5599;     // max=5562
    }

    @Override
    public HashFunction createHashFunction() {
        return new EdrHash(w, numOfPoints);
    }

    @Override
    public Integer combine(int[] hashes) {
        return Arrays.hashCode(hashes);
    }

    @Override
    public DistanceMeasure createDistanceMeasure() {
        return new EdrDistance();
    }

    @Override
    public String toString() {
        return "EdrHashFamily";
    }
}
