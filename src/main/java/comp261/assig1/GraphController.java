package comp261.assig1;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.*;

public class GraphController {

    // names from the items defined in the FXML file
    @FXML
    private TextField searchText;
    @FXML
    private Button english;
    @FXML
    private Button maori;

    @FXML
    private Button load;
    @FXML
    private Button quit;
    @FXML
    private Button up;
    @FXML
    private Button down;
    @FXML
    private Button left;
    @FXML
    private Button right;
    @FXML
    private Canvas mapCanvas;
    @FXML
    private Label nodeDisplay;
    @FXML
    private TextArea tripText;

    // These are use to map the nodes to the location on screen
    private Double scale = 5000.0; // 5000 gives 1 pixel ~ 2 meter
    private static final double ratioLatLon = 0.73; // in Wellington ratio of latitude to longitude
    private GisPoint mapOrigin = new GisPoint(174.77, -41.3); // Lon Lat for Wellington

    private static int stopSize = 5; // drawing size of stops
    private static int moveDistance = 100; // 100 pixels
    private static double zoomFactor = 1.1; // zoom in/out factor

    // added by me
    private ArrayList<Stop> highlightNodes = new ArrayList<Stop>();
    private ArrayList<Trip> highlightTrips = new ArrayList<Trip>();
    private boolean searchOn = false;

    // map model to screen using scale and origin
    private Point2D model2Screen(GisPoint modelPoint) {
        return new Point2D(model2ScreenX(modelPoint.lon), model2ScreenY(modelPoint.lat));
    }

    private double model2ScreenX(double modelLon) {
        return (modelLon - mapOrigin.lon) * (scale * ratioLatLon) + mapCanvas.getWidth() / 2;
    }

    // the getHeight at the start is to flip the Y axis for drawing as JavaFX draws
    // from the top left with Y down.
    private double model2ScreenY(double modelLat) {
        return mapCanvas.getHeight() - ((modelLat - mapOrigin.lat) * scale + mapCanvas.getHeight() / 2);
    }

    // map screen to model using scale and origin
    private double getScreen2ModelX(Point2D screenPoint) {
        return (((screenPoint.getX() - mapCanvas.getWidth() / 2) / (scale * ratioLatLon)) + mapOrigin.lon);
    }

    private double getScreen2ModelY(Point2D screenPoint) {
        return ((((mapCanvas.getHeight() - screenPoint.getY()) - mapCanvas.getHeight() / 2) / scale) + mapOrigin.lat);
    }

    // given but not used
    // private GisPoint getScreen2Model(Point2D screenPoint) {
    // return new GisPoint(getScreen2ModelX(screenPoint),
    // getScreen2ModelY(screenPoint));
    // }

    // change the displayed language to English
    public void toggleEn(ActionEvent event) throws Exception {
        System.out.println("Displaying in English " + event.getEventType());
        Stage stage = (Stage) mapCanvas.getScene().getWindow();
        Main m = new Main();
        m.loadEn(stage);
        event.consume();
    }

    // change the displayed language to Maori
    public void toggleMi(ActionEvent event) throws Exception {
        System.out.println("Displaying in Maori " + event.getEventType());
        Stage stage = (Stage) mapCanvas.getScene().getWindow();
        Main m = new Main();
        m.loadMi(stage);
        event.consume();
    }

    /* handle the load button being pressed connected using FXML */
    public void handleLoad(ActionEvent event) {
        Stage stage = (Stage) mapCanvas.getScene().getWindow();
        System.out.println("Handling event " + event.getEventType());
        FileChooser fileChooser = new FileChooser();
        // Set to user directory or go to default if cannot access

        File defaultNodePath = new File("data");
        if (!defaultNodePath.canRead()) {
            defaultNodePath = new File("C:/");
        }
        fileChooser.setInitialDirectory(defaultNodePath);
        FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extentionFilter);

        fileChooser.setTitle("Open Stop File");
        File stopFile = fileChooser.showOpenDialog(stage);

        fileChooser.setTitle("Open Stop Pattern File");
        File tripFile = fileChooser.showOpenDialog(stage);

        Main.graph = new Graph(stopFile, tripFile);
        drawGraph();
        event.consume(); // this prevents other handlers from being called
    }

    public void handleQuit(ActionEvent event) {
        System.out.println("Quitting with event " + event.getEventType());
        event.consume();
        System.exit(0);
    }

    public void handleZoomin(ActionEvent event) {
        System.out.println("Zoom in event " + event.getEventType());
        scale *= zoomFactor;
        drawGraph();
        event.consume();
    }

    public void handleZoomout(ActionEvent event) {
        System.out.println("Zoom out event " + event.getEventType());
        scale *= 1.0 / zoomFactor;
        drawGraph();
        event.consume();
    }

    public void handleUp(ActionEvent event) {
        System.out.println("Move up event " + event.getEventType());
        mapOrigin.add(0, moveDistance / scale);
        drawGraph();
        event.consume();
    }

    public void handleDown(ActionEvent event) {
        System.out.println("Move Down event " + event.getEventType());
        mapOrigin.subtract(0, moveDistance / scale);
        drawGraph();
        event.consume();
    }

    public void handleLeft(ActionEvent event) {
        System.out.println("Move Left event " + event.getEventType());
        mapOrigin.add(moveDistance / scale, 0);
        drawGraph();
        event.consume();
    }

    public void handleRight(ActionEvent event) {
        System.out.println("Move Right event " + event.getEventType());
        mapOrigin.subtract(moveDistance / scale, 0);
        drawGraph();
        event.consume();
    }

    public void handleSearch(ActionEvent event) {
        System.out.println("Look up event " + event.getEventType() + "  " + searchText.getText());
        highlightTrips.clear();
        String search = searchText.getText();
        for (Stop st : Main.graph.getStops()) {
            if (st.getName().equals(search)) {
                highlightNodes.clear();
                tripText.clear();
                printText(st);
                drawGraph();
            }
        }
        event.consume();
    }

    public void handleSearchKey(KeyEvent event) {
        tripText.clear();
        highlightTrips.clear();
        highlightNodes.clear();
        searchOn = true;
        System.out.println("Look up event " + event.getEventType() + "  " + searchText.getText());
        String search = searchText.getText();
        if (search.length() > 0) {
            List<Stop> currentList = Main.graph.trie.getAll(search);
            for (Stop st : currentList) {
                // if an exact match has been found
                if (currentList.size() == 1) {
                    searchOn = false;
                    printText(st);
                    drawGraph();
                    return;
                }

                Graph graph = Main.graph;
                for (Map.Entry<Trip, ArrayList<Stop>> entry : graph.routes.entrySet()) { // for all trips
                    Trip id = entry.getKey();
                    ArrayList<Stop> stopList = entry.getValue();
                    if (stopList.contains(st)) { // if the stop is on the trip
                        highlightNodes.add(st); // highlight it
                        highlightTrips.add(id);
                    }
                }
                String text = st.getId() + ": " + st.getName() + " at " + st.getLoc() + "\n";
                tripText.appendText(text);
                nodeDisplay.setText(st.getId() + ": " + st.getName());
            }
        }
        drawGraph();
        event.consume();
    }

    /*
     * handle mouse clicks on the canvas
     * select the node closest to the click
     */
    public void handleMouseClick(MouseEvent event) {
        searchOn = false;
        System.out.println("Mouse click event " + event.getEventType());
        Point2D screenPoint = new Point2D(event.getX(), event.getY());
        double x = getScreen2ModelX(screenPoint);
        double y = getScreen2ModelY(screenPoint);
        highlightClosestStop(x, y);
        event.consume();
    }

    public void mouseScroll(ScrollEvent event) {

        System.out.println("Mouse scroll event " + event.getEventType());
        if (scale + event.getDeltaY() * (scale / 200) > 0) { // so that it wont invert
            scale += event.getDeltaY() * (scale / 200);
        }
        drawGraph();
        event.consume();
    }

    public double dragStartY = 0;
    public double dragStartX = 0;

    public void handleMouseDrag(MouseEvent event) {
        System.out.println("Mouse drag event " + event.getEventType());
        // pan the map
        double dx = event.getX() - dragStartX;
        double dy = event.getY() - dragStartY;
        dragStartX = event.getX();
        dragStartY = event.getY();
        mapOrigin.lat += dy / (scale * ratioLatLon);
        mapOrigin.lon -= dx / scale;
        drawGraph();
        event.consume();

    }

    public void handleMousePressed(MouseEvent event) {
        System.out.println("Mouse pressed event " + event.getEventType());
        dragStartX = event.getX();
        dragStartY = event.getY();

        drawGraph();
        event.consume();

    }

    // find the Closest stop to the lon,lat postion
    public void highlightClosestStop(double lon, double lat) {
        double minDist = Double.MAX_VALUE;
        Stop closestStop = null;
        // find closest stop and highlight it
        for (Stop node : Main.graph.getStops()) {
            GisPoint current = node.getLoc();
            double dist = current.distance(lon, lat);
            if (dist < minDist) {
                minDist = dist;
                closestStop = node;
            }
            ;
        }
        // highlight the trips through this node
        if (closestStop != null) {
            highlightTrips.clear();
            highlightNodes.clear();
            tripText.clear();
            printText(closestStop);
            drawGraph();
        }
    }

    // prints the trips for the given stop and draw the graph
    public void printText(Stop stop) {
        for (Map.Entry<Trip, ArrayList<Stop>> entry : Main.graph.routes.entrySet()) { // for all trips
            Trip id = entry.getKey();
            ArrayList<Stop> st = entry.getValue();
            if (st.contains(stop)) { // if the stop is on the trip
                highlightNodes.add(stop);
                highlightTrips.add(id);
                String text = "Trip ID: " + id.getTripId() + " Stops: " + st + "\n";
                tripText.appendText(text);
            }
        }
        nodeDisplay.setText(stop.toString() + ": " + stop.getName());
        drawGraph();
    }

    /*
     * Drawing the graph on the canvas
     */
    public void drawCircle(double x, double y, int radius) {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.fillOval(x - radius / 2, y - radius / 2, radius, radius);
    }

    public void drawLine(double x1, double y1, double x2, double y2) {
        mapCanvas.getGraphicsContext2D().strokeLine(x1, y1, x2, y2);
    }

    // This will draw the graph in the graphics context of the mapcanvas
    public void drawGraph() {
        Graph graph = Main.graph;
        if (graph == null) {
            return;
        }
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
        gc.setLineWidth(1);
        drawNodes();
        drawEdges();
    }

    // called by drawGraph(), highlights the stops in highlighNodes
    public void drawNodes() {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        ArrayList<Stop> stopList = Main.graph.getStops();
        stopList.forEach(node -> {
            int size = stopSize;
            if (highlightNodes.contains(node)) {
                gc.setFill(Color.RED);
                size = stopSize * 2;
            } else {
                gc.setFill(Color.BLUE);
            }
            Point2D screenPoint = model2Screen(node.getLoc());
            drawCircle(screenPoint.getX(), screenPoint.getY(), size);
        });
    }

    // called by drawGraph(), draw edges for each trip /all edges for the graph
    public void drawEdges() {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        HashMap<Trip, ArrayList<Edge>> edgeTrip = Main.graph.tripsWithEdges;
        for (Trip trip : edgeTrip.keySet()) {
            Trip tr = trip;
            for (Edge e : edgeTrip.get(tr)) { // for each edge in the value list
                if (e.getTripId().equals(tr)) {
                    gc.setStroke(Color.BLACK);
                    gc.setLineWidth(1);
                    Point2D startPoint = model2Screen(e.getFromStop().getLoc());
                    Point2D endPoint = model2Screen(e.getToStop().getLoc());
                    drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
                    if (highlightTrips.contains(tr) && (!searchOn)) { // just draw on top of it man idk
                        drawTrip(e, tr.getColor(), gc);
                    }

                }

            }

        }
    }

    // called by drawEdges(), highlights the given edge
    public void drawTrip(Edge e, Color c, GraphicsContext gc) {
        gc.setStroke(c);
        gc.setLineWidth(5);
        Point2D startPoint1 = model2Screen(e.getFromStop().getLoc());
        Point2D endPoint1 = model2Screen(e.getToStop().getLoc());
        drawLine(startPoint1.getX(), startPoint1.getY(), endPoint1.getX(), endPoint1.getY());

    }

}
