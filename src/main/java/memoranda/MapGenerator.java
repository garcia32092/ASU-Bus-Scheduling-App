package main.java.memoranda;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.List;
import java.util.*;

public class MapGenerator extends JPanel {
    private List<Node> nodes;
    private List<Node> route;

    private BufferedImage image;

    public MapGenerator() {
        this.nodes = new ArrayList<>();
        this.route = new ArrayList<>();

        getImage("/map1.png");

        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
    }

    public MapGenerator(List<Node> nodes) {
        this.nodes = nodes;
        this.route = new ArrayList<>();

        getImage("/map1.png");

        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
    }

    public void getImage(String fileName) {
        try {
            image = ImageIO.read(getClass().getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter and Setters for List of nodes
     *
     * @return nodes
     */
    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
        repaint();
    }


//    public void addNode(String id, double latitude, double longitude) {
//        nodes.add(new Node(id, latitude, longitude));
//    }

    public void addRoutePoint(Node n) {
        route.add(n);
    }

    // TODO: Algorithm for finding the shortest path between 2 specified nodes needs to be implemented
    public List<Node> getShortestRoute(String sourceId, String destinationId) {
        List<Node> shortestPath = new ArrayList<>();

        // Return the sequence of nodes representing the shortest path
        return shortestPath;
    }

	public List<Node> getRoute() {
		return route;
	}

	public void setRoute(List<Node> route) {
		this.route = route;
	}

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        if (image != null) {
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
        }

        Stroke str1 = new BasicStroke(3f);
        g2d.setStroke(str1);
        
        // Draw route
        g2d.setColor(Color.RED);
        if (!route.isEmpty()) {
        	for (int i = 0; i < route.size() - 1; i++) {
                Node node1 = route.get(i);
                Node node2 = route.get(i + 1);
                System.out.println("X: " + node1.getX() + " Y: " + node1.getY());
                System.out.println("X: " + node2.getX() + " Y: " + node2.getY());
                g2d.drawLine(node1.getX() + 18/2, node1.getY() + 18/2, node2.getX() + 18/2, node2.getY() + 18/2);
            }
        }

        // Iterate over the nodes and draw them on the panel
        for (Node node : nodes) {
            g.setColor(Color.BLACK);
            if (node.isBusStop()) {
            	// Draw a dot for each node
                g.fillOval(node.getX(), node.getY(), 18, 18);
                // Draw the node's ID
                g.drawString(node.getId(), node.getX(), node.getY());
            }

        }
    }
}
