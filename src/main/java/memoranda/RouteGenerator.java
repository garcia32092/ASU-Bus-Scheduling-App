package main.java.memoranda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RouteGenerator {
	List<Node> nodes;
	
	public RouteGenerator(List<Node> nodes) {
		this.nodes = nodes;
	}

	public List<Node> shortestRoute(Node start, Node end) {
	    // Maps each node to the length of the shortest path from start to this node
	    Map<Node, Double> distances = new HashMap<>();
	    // Maps each node to the previous node in the shortest path from start to this node
	    Map<Node, Node> previousNodes = new HashMap<>();
	    // All the nodes that have yet to be visited
	    Set<Node> unvisited = new HashSet<>(nodes);

	    // Initialize all distances to infinity
	    for (Node node : nodes) {
	        distances.put(node, Double.POSITIVE_INFINITY);
	    }

	    // The distance from start to itself is 0
	    distances.put(start, 0.0);

	    // The current node
	    Node current = start;

	    while (!unvisited.isEmpty()) {
	        // Visit all the neighbors of the current node
	        for (Node neighbor : current.getNeighbors()) {
	            if (unvisited.contains(neighbor)) {
	                // Calculate the distance to this neighbor through the current node
	                double distance = distances.get(current) + distance(current, neighbor);
	                if (distance < distances.get(neighbor)) {
	                    // This is a shorter path to the neighbor, so update the distance
	                    distances.put(neighbor, distance);
	                    // Also update the previous node in the path
	                    previousNodes.put(neighbor, current);
	                }
	            }
	        }

	        // Mark the current node as visited
	        unvisited.remove(current);

	        if (current == end) {
	            // We've reached the end node, so we're done
	            break;
	        }

	        // Select the next node to visit
	        current = null;
	        double smallestDistance = Double.POSITIVE_INFINITY;
	        for (Node node : unvisited) {
	            double distance = distances.get(node);
	            if (distance < smallestDistance) {
	                smallestDistance = distance;
	                current = node;
	            }
	        }

	        if (current == null) {
	            // There's no way to reach the end node, so there's no path
	            return null;
	        }
	    }

	    // At this point, previousNodes contains the shortest path from start to end.
	    // We just need to extract it.
	    List<Node> path = new ArrayList<>();
	    Node node = end;
	    while (node != null) {
	        path.add(node);
	        node = previousNodes.get(node);
	    }
	    // The path is currently from end to start, so reverse it
	    Collections.reverse(path);
	    return path;
	}

	private double distance(Node node1, Node node2) {
	    double dx = node1.getX() - node2.getX();
	    double dy = node1.getY() - node2.getY();
	    return Math.sqrt(dx * dx + dy * dy);
	}
}
