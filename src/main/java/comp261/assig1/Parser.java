package comp261.assig1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * This utility class parses the files, and return the relevant data structure.
 * Internally it uses BufferedReaders instead of Scanners to read in the files,
 * as Scanners are slow.
 * 
 * @author Simon
 */
public class Parser {

    // read the stop file
    // tab separated stop descriptions
    // stop_id stop_code stop_name stop_desc stop_lat stop_lon zone_id stop_url
    // location_type parent_station stop_timezone

    public static ArrayList<Stop> parseStops(File nodeFile) {
        // data types to be returned to the graph
        ArrayList<Stop> stops = new ArrayList<>();
        try {
            // make a reader
            BufferedReader br = new BufferedReader(new FileReader(nodeFile));
            br.readLine(); // throw away the top line of the file
            String line;
            // read in each line of the file
            while ((line = br.readLine()) != null) {
                // tokenise the line by splitting it on tabs
                String[] tokens = line.split("[\t]");
                if (tokens.length >= 6) {
                    // process the tokens
                    String stopId = tokens[0];
                    String stopName = tokens[2];
                    double lat = Double.valueOf(tokens[4]);
                    double lon = Double.valueOf(tokens[5]);
                    GisPoint position = new GisPoint(lon, lat);
                    Stop current = new Stop(position, stopName, stopId);
                    stops.add(current);
                }
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException("file reading failed.");
        }
        return stops;
    }

    // parse the trip file
    // header: stop_pattern_id,stop_id,stop_sequence,timepoint
    public static ArrayList<Trip> parseTrips(File tripFile) {

        ArrayList<Trip> trips = new ArrayList<>();

        try {
            // make a reader
            BufferedReader br = new BufferedReader(new FileReader(tripFile));
            br.readLine(); // throw away the top line of the file.
            String line;
            // read in each line of the file
            while ((line = br.readLine()) != null) { // for each line of data
                // tokenise the line by splitting it at ",".
                String[] tokens = line.split("[,]");
                if (tokens.length >= 4) {
                    // process the tokens
                    String stopPatternId = tokens[0];
                    String stopId = tokens[1];
                    // dont rlly need these
                    // int stopSequence = Integer.parseInt(tokens[2]);
                    // String timepoint = tokens[3];
                    Trip current = new Trip(stopPatternId, stopId);
                    trips.add(current);
                }
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException("file reading failed.");
        }
        return trips;
    }
}

// code for COMP261 assignments