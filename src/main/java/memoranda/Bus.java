package main.java.memoranda;

public class Bus {
    private String busId;
    private int seats;
    private String assignedDriverId = null;

    private Driver driver;

    /**
     * Default constructor with busId input.
     * Average number of seats on a city bus is 30
     *
     * @param id busId of the bus
     */
    public Bus(String id) {
        this(id, 30);
        this.driver = null;
    }

    /**
     * Constructer for busId and number of seats input.
     *
     * @param ID    busId of the bus
     * @param seats number of seats on the bus
     */
    public Bus(int id, int seats) {
        this.driver = null;
    }

    public Bus(String id, int seats) {
        this.busId = id;
        this.seats = seats;
    }

    /**
     * Returns the busId of the bus.
     *
     * @return int busId
     */
    public String getId() {
        return busId;
    }

    /**
     * Returns the number of seats of the bus.
     *
     * @return int seats
     */
    public int getSeats() {
        return seats;
    }

    /**
     * Sets the assignedDriver with driverID.
     *
     * @param driverID String busId
     */
    public void setAssignedDriver(String driverId) {
        this.assignedDriverId = driverId;
    }

    /**
     * Gets the driver busId of the assigned driver if there exists one.
     *
     * @return String driverID
     */
    public String getAssignedDriverId() {
        return this.assignedDriverId;
    }

    /**
     * Returns true or false if there is an assigned driver.
     *
     * @return true or false
     */
    public boolean hasAssignedDriver() {
        return this.assignedDriverId != null;
    }

    /**
     * Removes the assignment of a driver to this bus.
     */
    public void removeAssignedDriver() {
        this.assignedDriverId = null;
    }

    @Override
    public String toString() {
        String info = "Bus busId: " + getId() + "\n" +
            "Bus seats: " + getSeats() + "\n\n";

        return info;
    }

    /**
     * Gets the driver's reference assigned to the Bus
     *
     * @return the Driver object or null if no assigned driver
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     * Setter for the driver variable
     *
     * @param d the Driver reference
     */
    public void setDriver(Driver d) {
        driver = d;
    }

    public static void main(String[] args) {
        //testing purposes
    /*public static void main (String [] args) {
        Bus dr = new Bus(1,55);
        System.out.println(dr.toString());*/
    }
}
