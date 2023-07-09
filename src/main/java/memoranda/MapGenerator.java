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
    private List<Route> routes;

    private BufferedImage image;

    public MapGenerator() {
        this.nodes = new ArrayList<>();
        this.route = new ArrayList<>();
        this.routes = new ArrayList<>();

        getImage("/map1.png");

        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
    }

    public MapGenerator(List<Node> nodes) {
        this.nodes = nodes;
        this.route = new ArrayList<>();
        this.routes = new ArrayList<>();

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

    public void addRoutePoint(Node n) {
        route.add(n);
    }

	public List<Node> getRoute() {
		return route;
	}

	public void setRoute(List<Node> route) {
		this.route = route;
	}
	
	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        double refLatitude = 0;
        double refLongitude = 0;
        for (Node node : nodes) {
            if (node.getId().equals("Reference")) {
                refLatitude = node.getLatitude();
                refLongitude = node.getLongitude();
            }
        }


        if (image != null) {
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
        }

        Stroke str1 = new BasicStroke(5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{9}, 0);
        g2d.setStroke(str1);
        
        // Draw route
        if (!routes.isEmpty()) {
        	for (Route route : routes) {
        		g2d.setColor(route.getColor());
            	List<Node> routeNodes = route.getNodes();
            	for (int i = 0; i < routeNodes.size() - 1; i++) {
                    Node node1 = routeNodes.get(i);
                    Node node2 = routeNodes.get(i + 1);
                    System.out.println("X: " + node1.getX() + " Y: " + node1.getY());
                    System.out.println("X: " + node2.getX() + " Y: " + node2.getY());
                    g2d.drawLine(node1.getX() + 16/2, node1.getY() + 16/2, node2.getX() + 16/2, node2.getY() + 16/2);
                }
            }
        }

        // Iterate over the nodes and draw them on the panel
        for (Node node : nodes) {
            if (node.getId().equals("Reference")) {
                continue;
            }
            // Scale the longitude and latitude to fit within the panel dimensions
            int x = (int) ((((refLongitude - node.getLongitude()) * -1) / 0.0000206) + 222);
            int y = (int) (((refLatitude - node.getLatitude()) / 0.00001706) + 135);
            node.setX(x);
            node.setY(y);

            // Draw a dot for each node
            g.setColor(Color.DARK_GRAY);
            g.fillOval(x, y, 15, 15);
//            g.fillOval(222, 140, 10, 10); // reference point
//            g.fillOval(385, 250, 10, 10);
//            g.fillOval(1053, 300, 10, 10);
//            g.fillOval(1053, 700, 10, 10);
//            g.fillOval(385, 700, 10, 10);

            // Draw the node's ID

            g.setColor(Color.BLACK);
            if (node.isBusStop()) {
            	// Draw a dot for each node
                g.fillOval(node.getX(), node.getY(), 16, 16);
                // Draw the node's ID
                Font font = new Font("Verdana", Font.BOLD, 12);
                g.setFont(font);
                FontMetrics metrics = g.getFontMetrics(font);
                int xPos = node.getX() - metrics.stringWidth(node.getStopName()) / 2;
                int yPos = node.getY() - metrics.getHeight() / 2 + metrics.getAscent() / 2;
                g.drawString(node.getStopName(), xPos, yPos);
            }
        }
    }
}
