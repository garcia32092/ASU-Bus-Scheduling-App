package main.java.memoranda;


import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.*;
import java.util.*;


public class JsonHandler {

    public List<Node> nodes;
    public List<String> nodesString;

    public List<String> driversToString;
    public List<String> busesToString;
    public ArrayList<Driver> driverList;
    public ArrayList<Bus> busList;
    private final ArrayList<Route> routeList;
    private int numOfNodes = -1;

    public JsonHandler() {
        nodes = new ArrayList<Node>();
        nodesString = new ArrayList<String>();
        driverList = new ArrayList<Driver>();
        driversToString = new ArrayList<String>();
        busList = new ArrayList<Bus>();
        busesToString = new ArrayList<String>();
        routeList = new ArrayList<Route>();

    }

    private JSONObject createJsonObject(Node node) {
        JSONObject object = new JSONObject();
        object.put("id", node.getId());
        object.put("lat", node.getLatitude());
        object.put("lon", node.getLongitude());
        return object;
    }

    public void readNodesFromJSON(String filename) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(
                new BufferedReader(
                    new InputStreamReader(
                        new FileInputStream(filename), "UTF-8")));


            JSONArray nodesArray = (JSONArray) jsonObject.get("nodes");


            for (Object obj : nodesArray) {
                JSONObject nodeObj = (JSONObject) obj;
                String id = (String) nodeObj.get("id");
                double latitude = Double.parseDouble((String) nodeObj.get("lat"));
                double longitude = Double.parseDouble((String) nodeObj.get("lon"));
                boolean isBusStop = (Boolean) nodeObj.get("isBusStop");
            	String busStopName = (String) nodeObj.get("stopName");
                if (isBusStop) {
                	addNodeString((String) nodeObj.get("stopName"));
                }
                addNode(id, latitude, longitude, isBusStop, busStopName);
            }
            
            setCoords();
            addNeighbors();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setCoords() {
    	double refLatitude = 0;
        double refLongitude = 0;
        for (Node node : nodes) {
            if (node.getId().equals("Reference")) {
                refLatitude = node.getLatitude();
                refLongitude = node.getLongitude();
            }
            // Scale the longitude and latitude to fit within the panel dimensions
            int x = (int) ((((refLongitude - node.getLongitude()) * -1) / 0.0000206) + 222);
            int y = (int) (((refLatitude - node.getLatitude()) / 0.00001706) + 135);
            node.setX(x);
            node.setY(y);
        }
    }
    
    private void addNeighbors() {
    	for (Node node : nodes) {
    		if (node.getId().equals("Reference")) {
    			continue;
    		}
    	    int nodeId = Integer.parseInt(node.getId());
    	    for (Node potentialNeighbor : nodes) {
    	    	if (potentialNeighbor.getId().equals("Reference")) {
        			continue;
        		}
    	        if (node != potentialNeighbor) {
    	            int neighborId = Integer.parseInt(potentialNeighbor.getId());
    	            if (Math.abs(nodeId - neighborId) <= 1) {
    	                node.addNeighbor(potentialNeighbor);
    	            } else if ((nodeId == 1 && neighborId == numOfNodes) || (nodeId == numOfNodes && neighborId == 1)) {
    	            	node.addNeighbor(potentialNeighbor);
    	            } else if ((nodeId == 32 && neighborId == 17) || (nodeId == 17 && neighborId == 32)) {
    	            	node.addNeighbor(potentialNeighbor);
    	            } else if ((nodeId == 34 && neighborId == 31) || (nodeId == 31 && neighborId == 34)) {
    	            	node.addNeighbor(potentialNeighbor);
    	            } else if ((nodeId == 34 && neighborId == 1) || (nodeId == 1 && neighborId == 34)) {
    	            	node.addNeighbor(potentialNeighbor);
    	            }
    	        }
    	    }
    	}
    }

    private JSONObject createNodePoint(String id) {
        JSONObject object = new JSONObject();


        Node selectedNode = nodes.stream()
            .filter(node -> node.getId().equals(id))
            .findFirst()
            .orElse(null);


        if (selectedNode != null) {
            object.put("id", selectedNode.getId());
            object.put("lat", selectedNode.getLatitude());
            object.put("lon", selectedNode.getLongitude());
        }

        return object;
    }


    public void writeRouteToJson(Route route) {
        try {
            // Read the existing JSON file and parse it into a JSONObject
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(
                new BufferedReader(
                    new InputStreamReader(
                        new FileInputStream("nodes1.json"), "UTF-8")));


            // Create a new JSONArray to store all routes (existing + new)
            JSONArray routesArray = new JSONArray();

            // Create a new JSONObject for the current route
            JSONObject currentRouteObj = new JSONObject();

            // Create a new JSONArray to store all nodes of the current route
            JSONArray currentRouteNodesArray = new JSONArray();
            JSONObject nodeObj = new JSONObject();
            nodeObj.put("Driver", route.getDriver().getName());
            nodeObj.put("Bus ID", route.getBus().getId());
            nodeObj.put("Stop Duration", route.getStopDuration());
            for (Node node : route.getNodes()) {

                nodeObj.put("Id", node.getId());
                nodeObj.put("Longitude", node.getLongitude());
                nodeObj.put("Latitude", node.getLatitude());

                currentRouteNodesArray.add(nodeObj);
            }

            // Add the nodes array to the current route object
            currentRouteObj.put("Route", currentRouteNodesArray);

            // Add the current route object to the routes array
            routesArray.add(currentRouteObj);

            // Get the existing routes from the JSON object (if any) and add them to routesArray
            JSONArray existingRoutesArray = (JSONArray) jsonObject.get("Routes");
            if (existingRoutesArray != null) {
                routesArray.addAll(existingRoutesArray);
            }

            // Update the routes array in the JSONObject
            jsonObject.put("Routes", routesArray);

            // Write the updated JSONObject to the file
            try (OutputStreamWriter writer =
                     new OutputStreamWriter(new FileOutputStream("nodes1.json"),"UTF-8")) {
                writer.write(jsonObject.toJSONString());
                writer.flush();
                // The writer will be automatically closed by the try-with-resources block
            } catch (IOException e) {
                // Handle the exception appropriately
                e.printStackTrace();
            }
            System.out.println("Routes have been written to and saved in nodes1.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readRoutesFromJSON() {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(
                new BufferedReader(
                    new InputStreamReader(
                        new FileInputStream("nodes1.json"), "UTF-8")));


            JSONArray routesArray = (JSONArray) jsonObject.get("Routes");
            List<Node> listOfNodes = new ArrayList<Node>();
            Double stopDuration = 0.0;
            for (Object obj : routesArray) {
                JSONObject routeObj = (JSONObject) obj;
                if (routeObj.get("Stop Duration").equals("Stop Duration")) {
                    stopDuration = (Double) routeObj.get("Stop Duration");
                }
                String id = (String) routeObj.get("Id");
                Double longitude = (Double) routeObj.get("Longitude");
                Double latitude = (Double) routeObj.get("Latitude");
                boolean isBusStop = (Boolean) routeObj.get("isBusStop");
            	String busStopName = (String) routeObj.get("stopName");
                listOfNodes.add(new Node(id, latitude, longitude, isBusStop, busStopName));
            }


            addRoute(listOfNodes, stopDuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRoute(List<Node> listOfNodes, Double sd) {
        this.routeList.add(new Route((ArrayList<Node>) listOfNodes, sd));
    }

    public List<String> getNodesString() {
        return nodesString;
    }

    public void readDriversFromJSON(String filename) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(
                new BufferedReader(
                    new InputStreamReader(
                        new FileInputStream(filename), "UTF-8")));


            JSONArray nodesArray = (JSONArray) jsonObject.get("drivers");

            for (Object obj : nodesArray) {
                JSONObject nodeObj = (JSONObject) obj;
                String id = (String) nodeObj.get("id");
                String name = (String) nodeObj.get("name");
                String phoneNumber = (String) nodeObj.get("phoneNumber");
                addDriverToString(name);
                addDriver(id, name, phoneNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readBusesFromJSON(String filename) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(
                new BufferedReader(
                    new InputStreamReader(
                        new FileInputStream(filename), "UTF-8")));
            JSONArray nodesArray = (JSONArray) jsonObject.get("buses");

            for (Object obj : nodesArray) {
                JSONObject nodeObj = (JSONObject) obj;
                String id = (String) nodeObj.get("id");
                int seats = Integer.valueOf((String) nodeObj.get("seats"));
                Bus tempBus = new Bus(id, seats);
                tempBus.setAssignedDriver((String) nodeObj.get("assignedDriver"));
                addBusToString(id);
                busList.add(tempBus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeDriversToJSON(String filename) {
        try {
            // Read the existing JSON file and parse it into a JSONObject
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(
                new BufferedReader(
                    new InputStreamReader(
                        new FileInputStream(filename), "UTF-8")));


            // Create a new JSONArray to store all drivers (existing + new)
            JSONArray driversArray = new JSONArray();

            // Add the new drivers to the driversArray
            for (Driver driver : driverList) {
                JSONObject driverObj = new JSONObject();
                driverObj.put("id", driver.getId());
                driverObj.put("name", driver.getName());
                driverObj.put("phoneNumber", driver.getPhoneNumber());

                driversArray.add(driverObj);
            }

            // Update the drivers array in the JSONObject
            jsonObject.put("drivers", driversArray);

            // Write the updated JSONObject to the file

            try (OutputStreamWriter writer =
                     new OutputStreamWriter(new FileOutputStream(filename),"UTF-8")) {
                writer.write(jsonObject.toJSONString());
                writer.flush();
                // The writer will be automatically closed by the try-with-resources block
            } catch (IOException e) {
                // Handle the exception appropriately
                e.printStackTrace();
            }
            System.out.println("Drivers have been written to and saved in nodes1.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeBusesToJSON(String filename) {
        try {
            // Read the existing JSON file and parse it into a JSONObject
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(
                new BufferedReader(
                    new InputStreamReader(
                        new FileInputStream(filename), "UTF-8")));


            // Create a new JSONArray to store all drivers (existing + new)
            JSONArray busesArray = new JSONArray();

            // Add the new drivers to the driversArray
            for (Bus bus : busList) {
                JSONObject busObj = new JSONObject();
                busObj.put("id", String.valueOf(bus.getId()));
                busObj.put("seats", String.valueOf(bus.getSeats()));
                busObj.put("assignedDriver", String.valueOf(bus.getAssignedDriverID()));

                busesArray.add(busObj);
            }

            // Update the drivers array in the JSONObject
            jsonObject.put("buses", busesArray);

            // Write the updated JSONObject to the file
            try (OutputStreamWriter writer =
                     new OutputStreamWriter(new FileOutputStream(filename),"UTF-8")) {
                writer.write(jsonObject.toJSONString());
                writer.flush();
                // The writer will be automatically closed by the try-with-resources block
            } catch (IOException e) {
                // Handle the exception appropriately
                e.printStackTrace();
            }
            System.out.println("Buses have been written to and saved in nodes1.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getDriversToString() {
        return driversToString;
    }

    public List<String> getBusesToString() {
        return busesToString;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Route> getRouteList() {
        return routeList;
    }

    public void addNodeString(String option) {
        nodesString.add(option);
    }

    private void addDriverToString(String name) {
        driversToString.add(name);
    }

    private void addBusToString(String id) {
        busesToString.add(id);
    }

    public void addNode(String id, double latitude, double longitude, boolean isBusStop, String stopName) {
        nodes.add(new Node(id, latitude, longitude, isBusStop, stopName));
        numOfNodes++;
        System.out.println("Nodes: " + numOfNodes);
    }

    public void addDriver(String id, String name, String phoneNumber) {
        driverList.add(new Driver(id, name, phoneNumber));
    }


    public ArrayList<Driver> getDriverList() {
        return driverList;
    }


    public ArrayList<Bus> getBusList() {
        return busList;
    }

}
