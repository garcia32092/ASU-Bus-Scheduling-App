package main.java.memoranda;

public class Driver {
    private final String id;
    private String name;
    private String phone;

    /**
     * Default constructor.
     *
     * @param id    int, max of 6 characters
     * @param name  String, max of 100 characters
     * @param phone String, max of 14 characters
     */
    public Driver(String id, String name, String phone) {
        if (id.length() < 6) {
            for (int i = id.length(); i < 6; i++) {
                id = "0" + id;
            }
        }
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    /**
     * Returns Driver id.
     *
     * @return int id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Returns Driver name.
     *
     * @return String name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns Driver phone number.
     *
     * @return String phoneNumber
     */
    public String getPhoneNumber() {
        return this.phone;
    }

    /**
     * Sets the name of the Driver if they have chosen a legal name change.
     *
     * @param newName String, new name of driver
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Sets the phone number of the driver if they change phone numbers.
     *
     * @param phone String, phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * toString override method.
     *
     * @return String of information about the driver
     */
    @Override
    public String toString() {

        String info = "Driver: " + getName() + "\n" +
            "id: " + getId() + "\n" +
            "Phone Number: " + getPhoneNumber() + "\n";
        return info;
    }

    //testing purposes
    /*public static void main (String [] args) {
        Driver dr = new Driver(1,"Andy","55555555");
        System.out.println(dr.toString());
    }*/
}
