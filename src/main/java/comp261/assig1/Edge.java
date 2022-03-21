package comp261.assig1;

// The edge class represents an edge in the graph.

public class Edge {
    private Stop fromStop;
    private Stop toStop;
    private Trip tripId;

    // constructor
    public Edge(Stop fromStop, Stop toStop, Trip tripId) {
        this.fromStop = fromStop;
        this.toStop = toStop;
        this.tripId = tripId;
    }

    // getters and setters
    public Stop getFromStop() {
        return this.fromStop;
    }

    public void setFromStop(Stop fromStop) {
        this.fromStop = fromStop;
    }

    public Stop getToStop() {
        return this.toStop;
    }

    public void setToStop(Stop toStop) {
        this.toStop = toStop;
    }

    public Trip getTripId() {
        return this.tripId;
    }

    public void setTripId(Trip tripId) {
        this.tripId = tripId;
    }

}
