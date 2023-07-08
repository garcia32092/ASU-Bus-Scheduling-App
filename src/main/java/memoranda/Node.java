package main.java.memoranda;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private String id;
	private String stopName;
	private double latitude;
	private double longitude;
    private int x;
    private int y;
    private boolean isBusStop;
    private List<Node> neighbors;

	/**
     * The default constructor for Node
     *
     * @param id  the ID associated with the specific node
     * @param lat the latitude coordinates of the Node
     * @param lon the longitude coordinates of the Node
     */
    public Node(String id, double latitude, double longitude, boolean isBusStop, String stopName) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isBusStop = isBusStop;
        this.stopName = stopName;
        x = -1;
        y = -1;
        neighbors = new ArrayList<>();
    }

    /**
     * Getter function for the String ID
     *
     * @return the ID associated with the node
     */
    public String getId() {
        return id;
    }

    /**
     * Getter function for the latitude value
     *
     * @return the latitude value of the Node
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Getter function for the longitude value
     *
     * @return the longitude value of the Node
     */
    public double getLongitude() {
        return longitude;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public boolean isBusStop() {
		return isBusStop;
	}
    
    public List<Node> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(Node node) {
        neighbors.add(node);
    }
    
    public String getStopName() {
    	return stopName;
    }

    /**
     * toString implementation:
     * (unique ID number of Node)
     * latitude: (latitude of Node)
     * longitude: (longitude of Node)
     *
     * @return the string
     */
    public String toString() {
        return "Node: " + id + "\nlatitude: " + latitude + "\nlongitude: " + longitude;
    }

    /**
     * equals implementation
     *
     * @param n the comparison Node
     * @return returns true if the latitude and longitude values are equal, else returns false
     */
    public boolean equals(Node n) {
        return latitude == n.getLatitude() && longitude == n.getLongitude();
    }
}
