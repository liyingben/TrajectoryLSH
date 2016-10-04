package com.github.sawyer.Core;

import com.github.sawyer.Hash.HashFamily;
import com.github.sawyer.Hash.HashFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An index contains one or more locality sensitive hash tables. These hash
 * tables contain the mapping between a combination of a number of hashes
 * (encoded using an integer) and a list of possible nearest neighbours.
 *
 * @author sunyue
 * @version 1.0    2016/9/27 14:49
 */
public class HashTable {

    /**
     * Contains the mapping between a combination of a number of hashes (encoded
     * using an integer) and a list of possible nearest neighbours
     */
    private HashMap<Integer, List<Trajectory>> hashTable;
    private HashFunction[] hashFunctions;
    private HashFamily hashFamily;


    /**
     * Initialize a new hash table, it needs a hash family and a number of hash
     * functions that should be used.
     *
     * @param hashFamily  The hash function family knows how to create new hash
     *                    functions, and is used therefore.
     * @param numOfHashes The number of hash functions that should be used.
     */
    public HashTable(HashFamily hashFamily, int numOfHashes) {
        this.hashFamily = hashFamily;
        hashTable = new HashMap<>();
        this.hashFunctions = new HashFunction[numOfHashes];
        for (int i = 0; i < numOfHashes; i++) {
            hashFunctions[i] = this.hashFamily.createHashFunction();
        }
    }

    public int getNumOfHashes() {
        return hashFunctions.length;
    }

    /**
     * add a trajectory to the Index
     *
     * @param trajectory the trajectory to added
     */
    public void add(Trajectory trajectory) {
        Integer combinedHash = hash(trajectory);
        if (!hashTable.containsKey(combinedHash)) {
            hashTable.put(combinedHash, new ArrayList<>());
        }
        hashTable.get(combinedHash).add(trajectory);

    }

    /**
     * Calculate the combined hash for a trajectory
     *
     * @param trajectory the trajectory
     * @return An integer representing a combined hash.
     */
    public Integer hash(Trajectory trajectory) {
        int[] hashes = new int[hashFunctions.length];
        for (int i = 0; i < hashFunctions.length; i++) {
            hashes[i] = hashFunctions[i].hash(trajectory);
        }
        return hashFamily.combine(hashes);
    }

    /**
     * Query the hash table for a trajectory. It calculates the hash for the trajectory,
     * and does a lookup in the hash table. If no candidates are found, an empty
     * list is returned, otherwise, the list of candidates is returned.
     *
     * @param query the query trajectory
     * @return If no candidates are found, an empty list is returned,
     * otherwise, the list of candidates is returned.
     */
    public List<Trajectory> query(Trajectory query) {
        Integer combinedHash = hash(query);
        if (hashTable.containsKey(combinedHash)) {
            return hashTable.get(combinedHash);
        } else {
            return new ArrayList<>();
        }
    }
}
