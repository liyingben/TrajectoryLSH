package com.github.sawyer.Core;

/**
 * Point to store trajectory point data.
 *
 * @author sunyue
 * @version 1.0    2016/9/27 14:11
 */
public class Point {
    private long timestamp;
    private double longitude;
    private double latitude;

    public Point(long timestamp, double longitude, double latitude) {
        this.timestamp = timestamp;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Calculates the dotSum product（scalar product）, of this point with the other point.
     *
     * @param other the other point
     * @return dot product
     */
    public double dot(Point other) {
        return this.getLatitude() * other.getLatitude() + this.getLongitude() * other.getLongitude();
    }

    /**
     * Calculates the euclidean distance of this point with the other point.
     * @param other the other point
     * @return euclidean distance
     */
    public double euclidean(Point other) {
        return Math.sqrt(Math.pow(this.getLatitude() - other.getLatitude(), 2) +
                Math.pow(this.getLongitude() - other.getLongitude(), 2));
    }

    @Override
    public String toString() {
        return "Point{" +
                "timestamp=" + timestamp +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
