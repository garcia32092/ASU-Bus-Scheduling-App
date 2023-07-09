package main.java.memoranda;

import java.awt.Color;

// Route(nodes, length, duration)
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Route {
    ArrayList<Node> nodes;
    private double length; // in km
    private double duration; // in minutes
    private Random rand;
    private Color color;
    
    private double stopDuration; // in minutes

    private Driver driver;
    private Bus bus;

    /**
     * Constructor for Route, uses an existing ArrayList of Node objects
     *
     * @param n  ArrayList of Nodes
     * @param sd stopDuration of Bus at each Node
     */
    public Route(List<Node> n, double sd) {
        this.nodes = (ArrayList<Node>) n;
        length = calculateLength();
        duration = calculateDuration();
        this.stopDuration = sd;
    	rand = new Random();
    	this.color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
    }

    /**
     * Constructor for Route specifying the starting Node
     *
     * @param initialNode The first Node on the Route
     * @param sd          stopDuration of Bus at each Node
     */
    public Route(Node initialNode, double sd) {
        this.nodes = new ArrayList<Node>();
        nodes.add(initialNode);
        length = calculateLength();
        duration = calculateDuration();
        this.stopDuration = sd;
    	rand = new Random();
    	this.color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
    }

    /**
     * Default constructor for route, only specifying the stopDuration of each stop
     *
     * @param sd stopDuration of Bus at each Node
     */

    public Route(double sd) {
        this.nodes = new ArrayList<Node>();
        length = 0;
        duration = 0;
        this.stopDuration = sd;
    	rand = new Random();
    	this.color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
    }

    /**
     * Getter and Setter functions for driver
     * assigned to this route
     */

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    /**
     * Getter and Setter for Bus assigned to
     * this route
     */
    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }


    /**
     * Getter function for the length of the Route
     *
     * @return the length of the Route in km
     */
    public double getLength() {

        return length;
    }

    /**
     * Getter function for the duration of the Route
     *
     * @return the approximate duration of the Route in minutes
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Getter function for the stopDuration of each stop on the Route
     *
     * @return the stopDuration in minutes
     */
    public double getStopDuration() {
        return stopDuration;
    }

    /**
     * Sets the stop duration for the Route in minutes
     *
     * @param sd the stop duration of the Route in minutes
     */
    public void setStopDuration(double sd) {
        stopDuration = sd;
    }

    /**
     * Returns an ArrayList of Nodes in the Route
     *
     * @return the ArrayList of Nodes in the Route
     */
    public List<Node> getNodes() {
        return nodes;
    }

    /**
     * Adds a node to the Route and updates the length of the Route and Duration
     *
     * @param n the Node being added to the Route
     */
    public void addNode(Node n) {
        nodes.add(n);
        length = calculateLength();
        duration = calculateDuration();
    }

    /**
     * Adds a Node at a certain index of the list of Nodes
     *
     * @param i the index where the Node should be added
     * @param n the Node to add
     * @return true if the Node is added in range of the ArrayList, false if index is out of range
     */
    public boolean addNodesAtIndex(int i, Node n) {
        if (i > 0 && i < nodes.size()) {
            nodes.add(i, n);
            return true;
        } else if (i == nodes.size()) {
            nodes.add(n);
        } else {
            return false;
        }
        return false;
    }

    /**
     * Removes the specified Node of the ArrayList
     *
     * @param n the Node to be removed
     * @return true if the Node is removed, and false if the Node isn't in the list
     */
    public boolean removeNode(Node n) {
        for (int i = 0; i < nodes.size(); ++i) {
            if (n.equals(nodes.get(i))) {
                nodes.remove(n);
                return true;
            }
        }
        return false;
    }


    /**
     * Removes the Node at the specified index of the ArrayList
     *
     * @param i the index of the Node to be removed
     * @return true if the Node can be removed from index, false if not.
     */
    public boolean removeNodeAtIndex(int i) {
        if (i > 0 && i < nodes.size()) {
            nodes.remove(i);
            return true;
        } else {
            return false;
        }
    }


    /**
     * Calculates the length of the Route in km using the distanceOfNodes() function
     */
    public double calculateLength() {
        double out = 0;
        for (int i = 0; i < nodes.size() - 1; ++i) {
            out += distance(nodes.get(i), nodes.get(i + 1));
        }
        return out;
    }
    
    private double distance(Node node1, Node node2) {
	    double dx = node1.getX() - node2.getX();
	    double dy = node1.getY() - node2.getY();
	    return Math.sqrt(dx * dx + dy * dy);
	}
    
    public Color getColor() {
    	return color;
    }

    /**
     * Based on cityobservatory.org, the average bus speed is 12.7 mph in cities (20.44 kph). This speed will be used to
     * calculate the duration of the route using the calculated length. t = d/s.
     * <p>
     * Duration also includes the duration of each stop.
     *
     * @return double: the approximate duration of the Route in minutes.
     */

    public double calculateDuration() {
        double distance = length;
        double speed = 30;
        double minutes = (distance / speed) * 60; // convert to minutes
        minutes += (stopDuration * nodes.size());
        return minutes;
    }


    /**
     * toString implementation:
     *
     * @return the String
     */
    public String toString() {
        String out = "";
        out += getDriver().toString() + " \n" + getBus().toString();
        for (int i = 0; i < nodes.size(); ++i) {
            out += nodes.get(i).toString() + "\n";
        }
        return out;
    }


    /**
     * equals implementation
     *
     * @param route the alternate Route to be compared
     * @return true if the Route contains the same Nodes in the same order, or false if not
     */
    public boolean equals(Route route) {
        // first check number of nodes
        if (!route.getNodes().equals(nodes)) {
            return false;
        }
        else {
            for (int i = 0; i < route.getNodes().size(); ++i) {
                if (!nodes.get(i).equals(route.getNodes().get(i))) {
                    return false;
                }
            }
            return true;
        }
    }
}
