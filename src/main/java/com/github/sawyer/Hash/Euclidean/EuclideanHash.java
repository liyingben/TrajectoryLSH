package com.github.sawyer.Hash.Euclidean;

import com.github.sawyer.Hash.HashFunction;
import com.github.sawyer.Core.Point;
import com.github.sawyer.Core.Trajectory;

import java.util.Random;

/**
 * @author sunyue
 * @version 1.0    2016/9/28 13:44
 */
public class EuclideanHash implements HashFunction {
    private Trajectory randomProjection;
    private int w;
    private int offset;

    public EuclideanHash(int w, int numOfPoints) {
        Random random = new Random();
        this.w = w;
        this.offset = random.nextInt(w);
        randomProjection = new Trajectory(numOfPoints);

        for (int i = 0; i < numOfPoints; i++) {
            long t = random.nextLong();
            double v1 = random.nextGaussian();
            double v2 = random.nextGaussian();
            randomProjection.setPoint(i, new Point(t, v1, v2));
        }
    }

    @Override
    public int hash(Trajectory trajectory) {
        double hashValue = (trajectory.dotSum(randomProjection) + offset) / (double) w;
        return (int) Math.round(hashValue);
    }

}
