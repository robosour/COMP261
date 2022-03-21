package comp261.assig1;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javafx.scene.paint.Color;

public class Graph {
    HashMap<Trip, ArrayList<Stop>> routes = new HashMap<>(); // tripID: list of stops on that trip
    HashMap<Trip, ArrayList<Edge>> tripsWithEdges = new HashMap<>(); // TripID: list of edges on that trip
    private ArrayList<String> singleTripID = new ArrayList<>(); // simple list of trip ids (no dupes)
    private ArrayList<Stop> stops; // list of all stops in data from Parser
    private ArrayList<Trip> trips; // list of all trips in data from PArser
    public Trie trie; //trie of stop names

    public Graph(ArrayList<Stop> stopList, ArrayList<Trip> trips) {
        this.stops = stopList;
        this.trips = trips;
    }

    // constructor post parsing
    public Graph() {

    }

    // constructor with parsing
    public Graph(File stopFile, File tripFile) {
        stops = Parser.parseStops(stopFile);
        trips = Parser.parseTrips(tripFile);
        buildStopList();
        buildWithTrie();
    }

    // just a method to generate colors
    private Color newColor() {
        Random rand = new Random();
        double r = rand.nextDouble();
        double g = rand.nextDouble();
        double b = rand.nextDouble();
        return new Color(r, g, b, 1);
    }


    // build tripWithEdges map
    private void buildEdges() {
        for (Map.Entry<Trip, ArrayList<Stop>> entry : routes.entrySet()) {
            Trip id = entry.getKey();
            id.addColor(newColor());
            ArrayList<Stop> st = entry.getValue();
            tripsWithEdges.put(id, new ArrayList<Edge>());
            int size = st.size();
            for (int i = 0; i <= size; i++) { // for all stops in the list
                int temp = i;
                if (((temp + 1) < (size)) && (st.get(temp + 1) != null)) {
                    Edge current = new Edge(st.get(temp), st.get(++temp), id);
                    tripsWithEdges.get(id).add(current); // add the edge
                }
            }
        }
    }

    // build simpleTripID list
    private void buildTripsList() {
        routes.clear();
        for (int i = 0; i < trips.size(); i++) { // for all trips
            String id = trips.get(i).getTripId();
            if (!singleTripID.contains(id)) {
                singleTripID.add(id);
                routes.put(trips.get(i), new ArrayList<>()); // make the tripId a key
            }
        }
    }

    // build the trie used for search
    public void buildWithTrie() {
        trie = new Trie();
        for (Stop st : stops) {
            trie.add(st);
        }
    }

    // build "routes" map
    private void buildStopList() {
        buildTripsList();
        for (Map.Entry<Trip, ArrayList<Stop>> entry : routes.entrySet()) { // for all sorted trips
            ArrayList<Stop> st = new ArrayList<>();
            Trip id = entry.getKey();
            for (int i = 0; i < trips.size(); i++) { // for all trips in data
                if (trips.get(i).getTripId().equals(id.getTripId())) {
                    for (int j = 0; j < stops.size(); j++) { // for all the stops in data
                        if ((!st.contains(stops.get(j))) && trips.get(i).getStopId().equals(stops.get(j).getId())) {
                            stops.get(j).addTrip(id);
                            routes.get(id).add(stops.get(j));
                        }
                    }
                }
            }
        }
        buildEdges();
    }

    // getters and setters

    public ArrayList<Stop> getStops() {
        return this.stops;
    }

    public void setStops(ArrayList<Stop> stops) {
        this.stops = stops;
    }

    public ArrayList<Trip> getTrips() {
        return this.trips;
    }

    public void setTrips(ArrayList<Trip> trips) {
        this.trips = trips;
    }

    public HashMap<Trip, ArrayList<Edge>> getEdges() {
        return this.tripsWithEdges;
    }

    public void setEdges(HashMap<Trip, ArrayList<Edge>> edges) {
        this.tripsWithEdges = edges;
    }

}
