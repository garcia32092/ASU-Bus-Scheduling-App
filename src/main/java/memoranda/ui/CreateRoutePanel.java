package main.java.memoranda.ui;

import main.java.memoranda.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;


public class CreateRoutePanel extends JPanel implements ActionListener {


    private JButton createButton;
    private JTextField stopDuration;
    private JsonHandler jsonHandler;
    private JList<String> routePointList;
    private JComboBox<Object> driverBox;
    private JComboBox<Object> busBox;
    private List<Node> listOfNodes;
    private MapGenerator mapGen;
    private List<Route> routes;

    public CreateRoutePanel() {
        try {
            jbInit(mapGen);
        } catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }

    public CreateRoutePanel(MapGenerator mapGen) {
        try {
            jbInit(mapGen);
        } catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }

    void jbInit(MapGenerator mapGen) throws Exception {

    	this.mapGen = mapGen;
        jsonHandler = new JsonHandler();
        String fileName = "nodes1.json";
        jsonHandler.readDriversFromJSON(fileName);
        jsonHandler.readBusesFromJSON(fileName);
        jsonHandler.readNodesFromJSON(fileName);
        listOfNodes = new ArrayList<>();
        routes = new ArrayList<>();
        buildPanel();
    }

    private void buildPanel() {
        setPreferredSize(new Dimension(500, 550));
        setBackground(Color.GRAY);

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        // Route Selection
        JPanel routeSelectionPanel = new JPanel();
        routeSelectionPanel.setLayout(new BorderLayout());

        JLabel routeLabel = new JLabel("Select stops for route: ");
        JLabel instructionsLabel = instructionsLabelMaker();
        routeSelectionPanel.add(routeLabel, BorderLayout.NORTH);
        routeSelectionPanel.add(routeSelectionList(), BorderLayout.CENTER);
        routeSelectionPanel.add(instructionsLabel, BorderLayout.SOUTH);

        mainPanel.add(routeSelectionPanel, BorderLayout.NORTH);

        // Other Components
        JPanel otherComponentsPanel = new JPanel();
        otherComponentsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel stopDurationLabel = new JLabel("Set Stop Duration: (Default = 5 min.)");
        stopDuration = new JTextField(10);
        stopDuration.setText("5");
        JLabel driverLabel = new JLabel("Select Driver Name:");

        JLabel busLabel = new JLabel("Select Bus Number:");

        otherComponentsPanel.add(stopDurationLabel, gbc);
        otherComponentsPanel.add(stopDuration, gbc);
        otherComponentsPanel.add(driverLabel, gbc);
        otherComponentsPanel.add(driverPanel(), gbc);
        otherComponentsPanel.add(busLabel, gbc);
        otherComponentsPanel.add(busPanel(), gbc);

        // Create Button
        JPanel buttonPanel = new JPanel();
        createButton = new JButton("Create Route");
        createButton.addActionListener(this);
        buttonPanel.add(createButton);

        mainPanel.add(otherComponentsPanel, BorderLayout.WEST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JLabel instructionsLabelMaker() {
        JLabel label = new JLabel("<html>To select multiple stops at once:<br>" +
            "SHIFT + mouse click or Control key + mouse click on desired stops<br>");
        return label;
    }


    private JScrollPane routeSelectionList() {
        routePointList = new JList(jsonHandler.getNodesString().toArray());
        routePointList.setVisibleRowCount(12); // Set the number of visible rows
        routePointList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        addRoutePointListListener();
        JScrollPane sp = new JScrollPane(routePointList);
        return sp;
    }

    public void addRoutePointListListener() {
        routePointList.addListSelectionListener(e -> {
            List<String> nodes = routePointList.getSelectedValuesList();

            if (!nodes.isEmpty()) {
            	listOfNodes.clear();
            	try {
                    for (String selectedNode : nodes) {
                        for (Node node : jsonHandler.getNodes()) {
                            if (node.getStopName().equals(selectedNode)) {
                                listOfNodes.add(node);
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            
            for (int i = 0; i < listOfNodes.size() - 1; i++) {
                Node node1 = listOfNodes.get(i);
                System.out.println("Node ID: " + node1.getId());
            }
            System.out.println("END");
        });
    }

    private JComboBox<Object> driverPanel() {
        driverBox = new JComboBox<>();
        DefaultComboBoxModel<Object> driverModel = new DefaultComboBoxModel<>();
        jsonHandler.getDriversToString().forEach(driverModel::addElement);
        driverBox.setModel(driverModel);
        driverBox.setVisible(true);
        driverBox.setBounds(10, 310, 100, 25);
        return driverBox;
    }

    private JComboBox<Object> busPanel() {
        busBox = new JComboBox<>();
        DefaultComboBoxModel<Object> busModel = new DefaultComboBoxModel<>();
        jsonHandler.getBusesToString().forEach(busModel::addElement);
        busBox.setModel(busModel);
        busBox.setVisible(true);
        busBox.setBounds(10, 360, 100, 25);
        return busBox;
    }

    private void createRoute() {
        String driverName = (String) driverBox.getSelectedItem();
        String busId = (String) busBox.getSelectedItem();
        double stopTime = Double.parseDouble(stopDuration.getText());
        
        routePointList.clearSelection();
        RouteGenerator routeGen = new RouteGenerator(jsonHandler.getNodes());
        List<Node> newRoute = routeGen.shortestRoute(listOfNodes.get(0), listOfNodes.get(listOfNodes.size() - 1));
        
        System.out.println("\nTesting for path within shortestRoute");
        System.out.println("SIZE: " + newRoute.size());
	    for (int i = 0; i < newRoute.size() - 1; i++) {
            System.out.println("Node ID: " + newRoute.get(i).getId());
            System.out.println("Node X: " + newRoute.get(i).getX() + " Node Y: " + newRoute.get(i).getY());
        }
        System.out.println("END path testing");
        
        if (newRoute.isEmpty())
        	System.out.println("The new route is empty.");
        
        Route route = new Route(newRoute, stopTime);

        for (Driver driver : jsonHandler.getDriverList()) {
            if (driver.getName().equals(driverName)) {
                route.setDriver(driver);
            }
        }

        for (Bus bus : jsonHandler.getBusList()) {
            if (bus.getId().equals(busId)) {
                route.setBus(bus);
            }
        }
        
        routes.add(route);
        jsonHandler.writeRouteToJson(route);
        mapGen.setRoutes(routes);
        mapGen.repaint();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == createButton) {
            createRoute();

        }

        message();
    }

    private void message() {

        JOptionPane.showConfirmDialog(null, "Route created successfully", "", JOptionPane.DEFAULT_OPTION);
    }

    public JTextField getStopDuration() {
        return stopDuration;
    }

    public JList<String> getRoutePointList() {
        return routePointList;
    }

    public JComboBox<Object> getDriverBox() {
        return driverBox;
    }

    public JButton getCreateButton() {
        return createButton;
    }

    public JComboBox<Object> getBusBox() {
        return busBox;
    }

    public List<Node> getListOfNodes() {
        return listOfNodes;
    }
    
    public List<Route> getRoutes() {
        return routes;
    }

    public JsonHandler getJsonHandler() {
        return jsonHandler;
    }

//    public static void main(String[] args) {
//
//        CreateRoutePanel createRoutePanel = new CreateRoutePanel();
//
//        JFrame frame = new JFrame("Create Route");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().add(createRoutePanel);
//        frame.pack();
//        frame.setVisible(true);
//    }

}
