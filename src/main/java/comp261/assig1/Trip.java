package comp261.assig1;

import java.util.ArrayList;

import javafx.scene.paint.Color;

public class Trip {
    
    private String tripId;
    private String stopId;
    ArrayList<Stop> stops = new ArrayList<>();
    ArrayList<Trip> trips;
    private Color col;

    //constructors
    public Trip(String tripId){
        this.tripId = tripId;
    }

    public Trip(String tripId, String stopId) {
        this.tripId = tripId;
        this.stopId = stopId;
    }

    //assign a color to the trip
    public void addColor(Color col){
        this.col = col; 
    }

    //add a stop to the trip
    public void addStop(Stop s){
        stops.add(s);
    }

    //getters and setters

    public Color getColor(){
        return this.col;
    }
    
    public String getTripId() {
        return this.tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getStopId() {
        return this.stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

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

    //for printing to tripText
    public String toString() {
        return "Trip ID: " + getTripId();
    }

}
