package main.java.memoranda.ui;

import main.java.memoranda.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class RoutePanel extends JPanel {

    private JScrollPane scrollPane;
    private JsonHandler jsonHandler;
    private MapGenerator mapGen;


    public RoutePanel() {
        try {
            jbInit();
        } catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }

    void jbInit() throws Exception {

        jsonHandler = new JsonHandler();
        String fileName = "nodes1.json";
        jsonHandler.readNodesFromJson(fileName);
        mapGen = new MapGenerator(jsonHandler.getNodes());
        scrollPane = new JScrollPane();

        this.setLayout(new BorderLayout());
        scrollPane.getViewport().setBackground(Color.DARK_GRAY);
        scrollPane.getViewport().add(mapGen);
        scrollPane.setPreferredSize(new Dimension(900, 800));
        mapGen.repaint();


        JScrollBar horizScrollBar = scrollPane.getHorizontalScrollBar();
        horizScrollBar.setUnitIncrement(25);
        horizScrollBar.setBlockIncrement(50);
        this.add(scrollPane, BorderLayout.CENTER);

        RoutePanel.PopupListener ppListener = new RoutePanel.PopupListener();
        scrollPane.addMouseListener(ppListener);

        buildSidePanel();
    }


    private void buildSidePanel() {
        // Side Panel
        CreateRoutePanel routePanel = new CreateRoutePanel(mapGen);
        JPanel sidePanel = new JPanel();

        sidePanel.add(routePanel);

        this.add(sidePanel, BorderLayout.EAST);
    }


    class PopupListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            //            if ((e.getClickCount() == 2) && (eventsTable.getSelectedRow() > -1))
            //                editEventB_actionPerformed(null);
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                //                eventPPMenu.show(e.getComponent(), e.getCoordinateX(), e.getCoordinateY());
            }
        }

    }


}
