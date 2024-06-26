package main.java.memoranda.ui;

import main.java.memoranda.*;
import main.java.memoranda.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BusAndDriverPanel extends JPanel {
    JButton createDriverButton, createBusButton;
    JScrollPane driverScrollPane, busScrollPane;
    JPanel columnPane, listPane, driverPane, busPane, driverColHeaders, busColHeaders;
    JsonHandler jsonHandler = new JsonHandler();
    DriverList driverList;
    BusList busList;
    Font font = new Font(Font.MONOSPACED, Font.BOLD, 14);
    String fileName = "nodes1.json";


    /**
     * Default Constructor.
     */
    public BusAndDriverPanel() {
        try {
            panelInitialization();
        } catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }

    /**
     * Initializes the panel and its respective lists.
     */
    private void panelInitialization() {
        //Import the drivers from the json file and populate driverList
        jsonHandler.readDriversFromJson(fileName);
        driverList = new DriverList(jsonHandler.driverList);
        jsonHandler.readBusesFromJson(fileName);
        busList = new BusList(jsonHandler.busList);

        //set the main panel layout to be a borderlayout
        this.setLayout(new BorderLayout());

        //########################################## Top Panel ##########################################
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(getWidth(), 90));
        topPanel.setBackground(Color.WHITE);

        //Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Color.WHITE);

        //Create Driver Button
        createDriverButton = new JButton();
        createDriverButton.setBackground(Color.WHITE);
        createDriverButton.setIcon(
            new ImageIcon(main.java.memoranda.ui.AppFrame.class.getResource("/ui/icons/Add_Driver.png")));
        createDriverButton.setToolTipText(Local.getString("Create a New Driver"));
        createDriverButton.setPreferredSize(new Dimension(120, 80));
        createDriverButton.setText("New Driver");
        createDriverButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createDriverButton_ActionPerformed(e);
            }
        });

        //Create Bus Button
        createBusButton = new JButton();
        createBusButton.setBackground(Color.WHITE);
        createBusButton.setIcon(new ImageIcon(main.java.memoranda.ui.AppFrame.class.getResource("/ui/icons/Add_Bus.png")));
        createBusButton.setToolTipText(Local.getString("Create a New Bus"));
        createBusButton.setPreferredSize(new Dimension(120, 80));
        createBusButton.setText("New Bus");
        createBusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createBusButton_ActionPerformed(e);
            }
        });

        // Create a placeholder panel for centering
        JPanel placeholderPanel = new JPanel(new GridBagLayout());
        placeholderPanel.setBackground(Color.WHITE);

        // Set horizontal gap between buttons
        buttonPanel.add(createDriverButton);
        buttonPanel.add(Box.createHorizontalStrut(875));
        buttonPanel.add(createBusButton);

        // Add button panel to the placeholder panel
        placeholderPanel.add(buttonPanel);

        // Add the placeholder panel to the top panel
        topPanel.add(placeholderPanel, BorderLayout.CENTER);

        //########################################## Columns ##########################################
        //displayPane the columnPanes **NONE OF THIS COLUMN STUFF WORKS YET**
        columnPane = new JPanel();
        columnPane.setLayout(new BoxLayout(columnPane, BoxLayout.X_AXIS));
        columnPane.setPreferredSize(new Dimension(getWidth(), 25));
        columnPane.setMinimumSize(new Dimension(getWidth(), 25));
        columnPane.setBackground(Color.WHITE);

        //Set up the columnPanes
        driverColHeaders = new JPanel();
        driverColHeaders.setLayout(new BorderLayout());
        driverColHeaders.setPreferredSize(new Dimension(getWidth(), getHeight()));

        busColHeaders = new JPanel();
        busColHeaders.setLayout(new BorderLayout());
        busColHeaders.setPreferredSize(new Dimension(getWidth(), getHeight()));

        //set up the columns
        JPanel idPane = new JPanel();
        idPane.setLayout(new GridLayout());
        JLabel driverID = new JLabel("Driver ID");
        idPane.add(driverID);
        driverID.setHorizontalAlignment(SwingConstants.CENTER);
        idPane.setBorder(BorderFactory.createRaisedBevelBorder()); // Set cell border
        idPane.setPreferredSize(new Dimension(84, idPane.getHeight()));

        JPanel driverInfoPane = new JPanel();
        driverInfoPane.setLayout(new BorderLayout());
        JLabel driverName = new JLabel("Driver Name");
        driverName.setHorizontalAlignment(SwingConstants.CENTER);
        driverName.setBorder(BorderFactory.createRaisedBevelBorder()); // Set cell border
        JLabel driverPhone = new JLabel("Driver Phone");
        driverPhone.setBorder(BorderFactory.createRaisedBevelBorder()); // Set cell border
        driverPhone.setHorizontalAlignment(SwingConstants.CENTER);
        driverPhone.setPreferredSize(new Dimension(130, driverPhone.getHeight()));
        driverInfoPane.add(driverName, BorderLayout.CENTER);
        driverInfoPane.add(driverPhone, BorderLayout.EAST);

        JPanel bufferLbl1 = new JPanel();
        bufferLbl1.setPreferredSize(new Dimension(91, bufferLbl1.getHeight()));

        JPanel borderPane = new JPanel();
        borderPane.setLayout(new BorderLayout());
        borderPane.add(idPane, BorderLayout.WEST);
        borderPane.add(driverInfoPane, BorderLayout.CENTER);
        borderPane.add(bufferLbl1, BorderLayout.EAST);

        //Add columns to driverColumns pane
        driverColHeaders.add(borderPane);

        JLabel driverId2 = new JLabel("Driver ID");
        driverId2.setHorizontalAlignment(SwingConstants.CENTER);
        driverId2.setBorder(BorderFactory.createRaisedBevelBorder()); // Set cell border
        driverId2.setPreferredSize(new Dimension(84, driverId2.getHeight()));

        JLabel driverName2 = new JLabel("Driver Name");
        driverName2.setHorizontalAlignment(SwingConstants.CENTER);
        driverName2.setBorder(BorderFactory.createRaisedBevelBorder()); // Set cell border
        driverName2.setPreferredSize(new Dimension(84, driverName2.getHeight()));

        JLabel busID = new JLabel("Bus ID");
        busID.setHorizontalAlignment(SwingConstants.CENTER);
        busID.setBorder(BorderFactory.createRaisedBevelBorder()); // Set cell border
        busID.setPreferredSize(new Dimension(83, busID.getHeight()));

        JLabel busSeats = new JLabel("Bus Seats");
        busSeats.setHorizontalAlignment(SwingConstants.CENTER);
        busSeats.setBorder(BorderFactory.createRaisedBevelBorder()); // Set cell border
        busSeats.setPreferredSize(new Dimension(84, busSeats.getHeight()));

        JPanel buttonHdrPane = new JPanel();
        buttonHdrPane.setLayout(new BorderLayout());

        JLabel buttonHeader = new JLabel("Select A Driver");
        buttonHeader.setHorizontalAlignment(SwingConstants.CENTER);
        buttonHeader.setBorder(BorderFactory.createRaisedBevelBorder()); // Set cell border
        buttonHeader.setPreferredSize(new Dimension(225, buttonHeader.getHeight()));

        JLabel bufferLbl2 = new JLabel();
        bufferLbl2.setPreferredSize(new Dimension(91, bufferLbl2.getHeight()));

        buttonHdrPane.add(buttonHeader, BorderLayout.WEST);
        buttonHdrPane.add(bufferLbl2, BorderLayout.EAST);

        JPanel busInfoPane = new JPanel();
        busInfoPane.setLayout(new BorderLayout());
        busInfoPane.add(busID, BorderLayout.WEST);
        busInfoPane.add(busSeats, BorderLayout.EAST);

        JPanel busInfoAndButtonHdrPane = new JPanel();
        busInfoAndButtonHdrPane.setLayout(new BorderLayout());
        busInfoAndButtonHdrPane.add(buttonHdrPane, BorderLayout.EAST);
        busInfoAndButtonHdrPane.add(busInfoPane, BorderLayout.WEST);

        busColHeaders.add(driverId2, BorderLayout.WEST);
        busColHeaders.add(driverName2, BorderLayout.CENTER);
        busColHeaders.add(busInfoAndButtonHdrPane, BorderLayout.EAST);

        //Add columnPanes to displayPane
        columnPane.add(driverColHeaders);
        columnPane.add(busColHeaders);
        topPanel.add(columnPane, BorderLayout.SOUTH);


        //########################################## Lists ##########################################
        //listPane is set as a horizontal boxlayout
        listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.X_AXIS));
        listPane.setBackground(Color.WHITE);

        //Driver List
        driverPane = new JPanel();
        driverPane.setLayout(new GridLayout(50, 1));
        driverPane.setBackground(Color.WHITE);

        //Bus List
        busPane = new JPanel();
        busPane.setLayout(new GridLayout(50, 1));
        busPane.setBackground(Color.WHITE);

        //Update list and add both bus and driver panes to their respective lists
        updateList();
        driverScrollPane = new JScrollPane(driverPane);
        driverScrollPane.setPreferredSize(new Dimension(getWidth(), getHeight()));
        busScrollPane = new JScrollPane(busPane);
        busScrollPane.setPreferredSize((new Dimension(getWidth(), getHeight())));

        //Edit the scroll bars for each scrollpane
        JScrollBar driverVertScrollBar = driverScrollPane.getVerticalScrollBar();
        driverVertScrollBar.setUnitIncrement(25);
        driverVertScrollBar.setBlockIncrement(50);
        JScrollBar busVertScrollBar = busScrollPane.getVerticalScrollBar();
        busVertScrollBar.setUnitIncrement(25);
        busVertScrollBar.setBlockIncrement(50);

        //Add lists to the listPanel
        listPane.add(driverScrollPane);
        listPane.add(busScrollPane);


        //add top panel and lists to main panel
        this.add(topPanel, BorderLayout.NORTH);
        this.add(listPane);
    }

    /**
     * Opens a pop-up window to create a bus.
     *
     * @param e Action Event
     */
    private void createBusButton_ActionPerformed(ActionEvent e) {
        BusDialog dialogBox = new BusDialog(App.getFrame(), Local.getString("New Bus"));
        Dimension frmSize = App.getFrame().getSize();
        Point loc = App.getFrame().getLocation();
        dialogBox.setLocation((frmSize.width - dialogBox.getSize().width) / 2 + loc.x, (frmSize.height - dialogBox.getSize().height) / 2 + loc.y);
        dialogBox.setVisible(true);
        if (dialogBox.CANCELLED)
            return;
        if (busList.hasBus(dialogBox.tempBus.getId()))
            return; //temp solution so no duplicate IDs are made
        busList.addBus(dialogBox.tempBus);

        jsonHandler.writeBusesToJson(fileName);

        updateList();
    }

    /**
     * Opens a pop-up window to create a driver.
     *
     * @param e ActionEvent
     */
    private void createDriverButton_ActionPerformed(ActionEvent e) {
        DriverDialog dialogBox = new DriverDialog(App.getFrame(), Local.getString("New Driver"));
        //Driver object has (int) ID, (String) Name, (String) Phone Number

        Dimension frmSize = App.getFrame().getSize();
        Point loc = App.getFrame().getLocation();
        dialogBox.setLocation((frmSize.width - dialogBox.getSize().width) / 2 + loc.x, (frmSize.height - dialogBox.getSize().height) / 2 + loc.y);
        dialogBox.setVisible(true);
        if (dialogBox.CANCELLED)
            return;
        if (driverList.hasDriver(dialogBox.tempDriver.getId()))
            return; //temp solution so no duplicate IDs are made
        driverList.addDriver(dialogBox.tempDriver);

        jsonHandler.writeDriversToJson(fileName);

        updateList();
    }

    public class OptionsDropdown extends JComboBox {
        String choices;
        Driver driver;
        Bus bus;

        public OptionsDropdown(Driver d, String c) {
            bus = null;
            driver = d;
            choices = c;

            //this.setSize(500, 500);
        }
    }

    /**
     * Internal class to associate a delete button with a driver.
     */
    public class DeleteButton extends JButton {
        private final Driver driver;
        private final Bus bus;

        public DeleteButton(Driver driver) {
            this.setBackground(Color.red);
            this.setPreferredSize(new Dimension(75, 25));
            this.setText("Delete");
            this.driver = driver;
            this.bus = null;
            this.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    deleteButton_ActionPerformed(e);
                }
            });
        }

        public DeleteButton(Bus bus) {
            this.setBackground(Color.red);
            this.setPreferredSize(new Dimension(75, 25));
            this.setText("Delete");
            this.bus = bus;
            this.driver = null;
            this.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    deleteButton_ActionPerformed(e);
                }
            });
        }

        private void deleteButton_ActionPerformed(ActionEvent e) {
            if (this.bus == null) {
                driverList.removeDriver(this.driver);
                jsonHandler.writeDriversToJson("nodes1.json");
            } else {
                busList.removeBus(this.bus);
                jsonHandler.writeBusesToJson("nodes1.json");
            }
            updateList();
        }
    }

    /**
     * Updates the displayed list(s).
     */
    private void updateList() {
        //Remove all items from both lists
        driverPane.removeAll();
        busPane.removeAll();

        //Update the driverPane
        for (Driver driver : driverList) {
            //Create a temporary panel to hold all the driver information
            JPanel borderPane = new JPanel();
            borderPane.setLayout(new BorderLayout());

            //Create the individual sections for the driver information and the delete button
            JLabel driverID = new JLabel(driver.getId());
            driverID.setFont(font);
            driverID.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY)); // Set cell border

            JLabel driverName = new JLabel(" " + driver.getName());
            driverName.setFont(font);
            driverName.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY)); // Set cell border

            JLabel driverPhone = new JLabel(driver.getPhoneNumber());
            driverPhone.setFont(font);
            driverPhone.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY)); // Set cell border
            driverPhone.setPreferredSize(new Dimension(129, driverPhone.getHeight()));
            driverPhone.setHorizontalAlignment(SwingConstants.CENTER);

            // Create a panel for the driver ID
            JPanel idPane = new JPanel();
            idPane.setLayout(new GridLayout(1, 1));
            idPane.add(driverID);
            idPane.setPreferredSize(new Dimension(83, idPane.getHeight()));
            driverID.setHorizontalAlignment(SwingConstants.CENTER);

            // Add the driver information to the temporary pane
            JPanel driverInfoPane = new JPanel();
            driverInfoPane.setLayout(new BorderLayout());

            driverInfoPane.add(driverName, BorderLayout.CENTER);
            driverInfoPane.add(driverPhone, BorderLayout.EAST);

            borderPane.add(idPane, BorderLayout.WEST);
            borderPane.add(driverInfoPane, BorderLayout.CENTER);

            DeleteButton deleteButton = new DeleteButton(driver);

            borderPane.add(deleteButton, BorderLayout.EAST);

            driverPane.add(borderPane);
        }
        driverPane.revalidate();
        driverPane.repaint();

        //Update the busPane
        for (Bus bus : busList) {
            //Create a temporary panel to hold all the driver information
            JPanel busBorderPane = new JPanel();
            busBorderPane.setLayout(new BorderLayout());

            //Create the individual sections for the driver information and the delete button
            JLabel busID = new JLabel(String.valueOf(bus.getId()));
            busID.setFont(font);
            busID.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY)); // Set cell border
            JLabel busSeats = new JLabel(String.valueOf(bus.getSeats()));
            busSeats.setFont(font);
            busSeats.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY)); // Set cell border

            JLabel assignedDriverID = new JLabel("------");
            assignedDriverID.setFont(font);
            if (bus.hasAssignedDriver())
                assignedDriverID.setText(bus.getAssignedDriverId());
            assignedDriverID.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY)); // Set cell border
            JLabel assignedDriverName = new JLabel("  ------");
            assignedDriverName.setFont(font);
            if (bus.hasAssignedDriver())
                if (driverList.hasDriver(bus.getAssignedDriverId()))
                    assignedDriverName.setText("  " + driverList.getDriver(bus.getAssignedDriverId()).getName()); //WHY THIS NO WORK!?!?!?!?!?
            assignedDriverName.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY)); // Set cell border

            DeleteButton deleteButton = new DeleteButton(bus);
            SelectDriverButton selectDriverB = new SelectDriverButton(bus);

            JPanel busIdPane = new JPanel();
            busIdPane.setLayout(new GridLayout(1, 1));
            busIdPane.add(busID);
            busIdPane.setPreferredSize(new Dimension(83, busIdPane.getHeight()));
            busID.setHorizontalAlignment(SwingConstants.CENTER);

            JPanel busSeatsPane = new JPanel();
            busSeatsPane.setLayout(new GridLayout(1, 1));
            busSeatsPane.add(busSeats);
            busSeatsPane.setPreferredSize(new Dimension(83, busSeatsPane.getHeight()));
            busSeats.setHorizontalAlignment(SwingConstants.CENTER);

            JPanel driverIdPane = new JPanel();
            driverIdPane.setLayout(new GridLayout(1, 1));
            driverIdPane.add(assignedDriverID);
            driverIdPane.setPreferredSize(new Dimension(83, driverIdPane.getHeight()));
            assignedDriverID.setHorizontalAlignment(SwingConstants.CENTER);

            JPanel busInfoPane = new JPanel();
            busInfoPane.setLayout(new BorderLayout());
            busInfoPane.add(busIdPane, BorderLayout.WEST);
            busInfoPane.add(busSeatsPane, BorderLayout.EAST);

            JPanel buttonsPane = new JPanel();
            buttonsPane.setLayout(new BorderLayout());
            buttonsPane.add(deleteButton, BorderLayout.EAST);
            buttonsPane.add(selectDriverB, BorderLayout.WEST);

            JPanel busInfoAndButtonsPane = new JPanel();
            busInfoAndButtonsPane.setLayout(new BorderLayout());
            busInfoAndButtonsPane.add(busInfoPane, BorderLayout.WEST);
            busInfoAndButtonsPane.add(buttonsPane, BorderLayout.EAST);

            //Add the items to the temporary pane
            busBorderPane.add(driverIdPane, BorderLayout.WEST);
            busBorderPane.add(assignedDriverName, BorderLayout.CENTER);
            busBorderPane.add(busInfoAndButtonsPane, BorderLayout.EAST);

            //Add the tempPane to the driverPane
            busPane.add(busBorderPane);
        }
        busPane.revalidate();
        busPane.repaint();
    }

    /**
     * Button that allows for the selection of a driver.
     */
    public class SelectDriverButton extends JButton {
        private final Bus bus;

        public SelectDriverButton(Bus bus) {
            this.bus = bus;
            this.setPreferredSize(new Dimension(225, 25));
            if (bus.hasAssignedDriver())
                this.setText("Change Driver");
            else
                this.setText("Add Driver");
            this.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selectDriverButton_ActionPerformed(e);
                }
            });
        }

        private void selectDriverButton_ActionPerformed(ActionEvent e) {
            AddDriverToBusDialog dialogBox = new AddDriverToBusDialog(App.getFrame(), Local.getString("Add Driver"), this.bus);
            Dimension frmSize = App.getFrame().getSize();
            Point loc = App.getFrame().getLocation();
            dialogBox.setLocation((frmSize.width - dialogBox.getSize().width) / 2 + loc.x, (frmSize.height - dialogBox.getSize().height) / 2 + loc.y);
            dialogBox.setVisible(true);
            if (dialogBox.CANCELLED)
                return;

            updateList();
        }
    }
}
