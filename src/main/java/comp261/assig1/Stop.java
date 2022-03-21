package comp261.assig1;

import java.util.ArrayList;


// decide the data structure for stops
public class Stop { 
    private GisPoint loc;
    private String name;
    private String id;
    private ArrayList<Trip> trips = new ArrayList<>();


        
    // Constructor
    public Stop(GisPoint loc, String name, String id) {
        this.loc = loc;
        this.name = name;
        this.id = id;
    }

    /** add a trip to the stop */
    public void addTrip(Trip tr){
        trips.add(tr);
    }

    /**
     * Get list of trips
     */
    public ArrayList<Trip> getTrips(){
        return this.trips;
    }

    // getters and setters 
    public GisPoint getLoc() {
        return loc;
    }

    public void setLoc(GisPoint loc) {
        this.loc = loc;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
    public String toString() {
        return getId();
    }

   

        
    }
