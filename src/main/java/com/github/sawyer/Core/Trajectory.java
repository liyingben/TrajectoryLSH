package com.github.sawyer.Core;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Trajectory serves as the main data structure that is stored and retrieved.
 * It contains an optional identifier (id) and a number of Points.
 *
 * @author sunyue
 * @version 1.0    2016/9/27 14:13
 */
public class Trajectory implements Serializable {

    private static final long serialVersionUID = -1429008753280662904L;

    /**
     * An optional id, identifier for the trajectory.
     */
    private String id;

    /**
     * Trajectory data points
     */
    private Point[] points;

    /**
     * constructor
     *
     * @param id     the id of the trajectory
     * @param points the data points
     */
    public Trajectory(String id, Point[] points) {
        this.id = id;
        this.points = points;
    }

    /**
     * copy constructor
     *
     * @param other the other trajectory
     */
    public Trajectory(Trajectory other) {
        this(other.getId(), Arrays.copyOf(other.getPoints(), other.length()));
    }

    /**
     * construct the trajectory with requested number of data points
     *
     * @param numOfPoints requested number of data points
     */
    public Trajectory(int numOfPoints) {
        this(null, new Point[numOfPoints]);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPoint(int index, Point point) {
        this.points[index] = point;
    }

    public Point getPoint(int index) {
        return this.points[index];
    }

    public Point[] getPoints() {
        return this.points;
    }

    public int length() {
        return this.points.length;
    }


    /**
     * Calculates the dotSum product sum（scalar product ）, of this trajectory with the other trajectory.
     *
     * @param other the other trajectory, which has more points than(>=) this trajectory.
     * @return dotSum product
     * @throws IndexOutOfBoundsException when the two trajectories do not have the same length.
     */
    public double dotSum(Trajectory other) {
        if (other.length() < this.length()) {
            throw new IndexOutOfBoundsException("The other trajectory has smaller length !!");
        }

        double sum = 0;
        Point[] onePoints = this.getPoints();
        Point[] otherPoints = other.getPoints();
        for (int i = 0; i < onePoints.length; i++) {
            sum += onePoints[i].dot(otherPoints[i]);
        }
        return sum;
    }

    @Override
    public String toString() {
        return "Trajectory{" +
                "id='" + id + '\'' +
                ", points=" + Arrays.toString(points) +
                '}';
    }
}
