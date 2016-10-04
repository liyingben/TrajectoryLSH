package com.github.sawyer.Hash;

import com.github.sawyer.Core.Trajectory;

/**
 * A hash function can hash a trajectory of arbitrary points to an integer
 * representation. The hash function needs to be locality sensitive to work in
 * the locality sensitive hash scheme. Meaning that vectors that are 'close'
 * according to some metric have a high probability to end up with the same hash.
 *
 * @author sunyue
 * @version 1.0    2016/9/27 14:42
 */
public interface HashFunction {
    /**
     * Hashes a trajectory of arbitrary points to an integer. The hash function
     * needs to be locality sensitive to work in the locality sensitive hash (LSH)
     * scheme. Meaning that trajectory that are 'close' according to some metric
     * have a high probability to end up with the same hash.
     *
     * @param trajectory the rajectory to hash
     * @return A locality sensitive hash (LSH)
     */
    int hash(Trajectory trajectory);
}
