package main.java.memoranda;

import java.util.*;

public class BusList implements Iterable<Bus> {
    private final List<Bus> buses;

    public BusList() {
        buses = new ArrayList<>();
    }

    public BusList(ArrayList<Bus> busList) {
        buses = busList;
    }

    public void addBus(Bus bus) {
        buses.add(bus);
    }

    public void removeBus(Bus bus) {
        buses.remove(bus);
    }

    public int getNumberOfBuses() {
        return buses.size();
    }

    public boolean hasBus(String id) {
        return getBus(id) != null;
    }

    public Bus getBus(String id) {
        for (int i = 0; i < buses.size(); ++i) {
            if (buses.get(i).getId().equals(id)) {
                return buses.get(i);
            }
        }
        return null;
    }

    @Override
    public Iterator<Bus> iterator() {
        return buses.iterator();
    }
}
