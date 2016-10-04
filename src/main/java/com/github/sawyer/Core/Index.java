package com.github.sawyer.Core;

import com.github.sawyer.Hash.DistanceMeasure;
import com.github.sawyer.Hash.HashFamily;
import com.github.sawyer.Util.SearchUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The index makes it easy to store trajectory and lookup queries efficiently. For
 * the moment the index is stored in memory. It holds a number of hash tables,
 * each with a couple of hashes. Together they can be used for efficient lookup
 * of nearest neighbours.
 *
 * @author sunyue
 * @version 1.0    2016/9/27 14:47
 */
public class Index {

    private int evaluated;
    private HashFamily hashFamily;
    private List<HashTable> hashTables;

    /**
     * Constructor
     *
     * @param hashFamily      The family of hash functions to use.
     * @param numOfHashes     The number of hashes that are concatenated in each hash table.
     *                        More concatenated hashes means that less candidates are
     *                        selected for evaluation.
     * @param numOfHashTables The number of hash tables in use, recall increases with the
     *                        number of hash tables. Memory use also increases. Time needed
     *                        to compute a hash also increases marginally.
     */
    public Index(HashFamily hashFamily, int numOfHashes, int numOfHashTables) {
        this.hashFamily = hashFamily;
        this.evaluated = 0;
        this.hashTables = new ArrayList<>();
        for (int i = 0; i < numOfHashTables; i++) {
            hashTables.add(new HashTable(hashFamily, numOfHashes));
        }
    }

    public int getNumOfHashes() {
        return hashTables.get(0).getNumOfHashes();
    }

    public int getNumOfHashTables() {
        return hashTables.size();
    }

    /**
     * The number of near neighbour candidates that are evaluated during the queries on this index.
     * Can be used to calculate the average evaluations per query.
     *
     * @return The number of near neighbour candidates that are evaluated during the queries on this index.
     */
    public int getTouched() {
        return evaluated;
    }

    /**
     * Add a trajectory to the current index. The hashes are calculated
     * with the current hash family and added in the right place.
     *
     * @param trajectory the trajectory to add
     */
    public void index(Trajectory trajectory) {
        for (HashTable table : hashTables) {
            table.add(trajectory);
        }
    }

    /**
     * Query for the k nearest neighbours in using the current index. The
     * performance (in computing time and recall/precision) depends mainly on
     * how the current index is constructed and how the underlying data looks.
     *
     * @param query The query trajectory. The center of the neighbourhood.
     * @param k     The maximum number of neighbours to return. Beware, the number
     *              of neighbours returned lays between zero and the chosen k.
     * @return A list of nearest neighbours
     */
    public List<Trajectory> query(final Trajectory query, int k) {
        Set<Trajectory> candidateSet = new HashSet<>();
        for (HashTable table : hashTables) {
            List<Trajectory> trajectories = table.query(query);
            candidateSet.addAll(trajectories);
        }

        List<Trajectory> candidateList = new ArrayList<>(candidateSet);

        evaluated += candidateList.size();

        DistanceMeasure measure = hashFamily.createDistanceMeasure();
        candidateList = SearchUtils.linearSearch(candidateList, query, k, measure);

//        DistanceComparator comparator = new DistanceComparator(queryTraj, measure);
//        Collections.sort(candiateList, comparator);
//
//        if (candiateList.size() > k) {
//            candiateList = candiateList.subList(0, k);
//        }
        return candidateList;
    }

}
