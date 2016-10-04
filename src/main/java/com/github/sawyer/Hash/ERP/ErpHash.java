package com.github.sawyer.Hash.ERP;

import com.github.sawyer.Core.Point;
import com.github.sawyer.Core.Trajectory;
import com.github.sawyer.Hash.HashFunction;

import java.util.Random;

/**
 * @author sunyue
 * @version 1.0    2016/10/4 14:14
 */
public class ErpHash implements HashFunction {
    private Trajectory randomProjection;
    private int w;
    private int offset;

    public ErpHash(int w, int numOfPoints) {
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
