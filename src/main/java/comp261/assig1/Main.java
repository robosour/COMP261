package comp261.assig1;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    // save the graph datastructure here as it is easy to get to using Main.graph
    public static Graph graph;
 
    //resets the stage to display language in english
    public void loadEn(Stage primaryStage) throws Exception {
        Locale localeEn = new Locale("en", "NZ");
        ResourceBundle bundle = ResourceBundle.getBundle("comp261/assig1/resources/strings", localeEn);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MapView.fxml"), bundle);
        Parent root  = (Parent) loader.load();

        primaryStage.setTitle(bundle.getString("title")); // set the title of the window from the bundle
        primaryStage.setScene(new Scene(root, 800, 700));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });

        graph = new Graph(new File("assig-1-maven/data/stops.txt"), new File("assig-1-maven/data/stop_patterns.txt"));
        ((GraphController) loader.getController()).drawGraph();
    }

    //resets the stage to dislay the language in maori
    public void loadMi(Stage primaryStage) throws Exception {
        Locale localeMaori = new Locale("mi", "NZ");
        ResourceBundle bundle = ResourceBundle.getBundle("comp261/assig1/resources/strings", localeMaori);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MapView.fxml"), bundle);
        Parent root  = (Parent)loader.load();

        primaryStage.setTitle(bundle.getString("title")); // set the title of the window from the bundl
        primaryStage.setScene(new Scene(root, 800, 700));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });
        graph = new Graph(new File("assig-1-maven/data/stops.txt"), new File("assig-1-maven/data/stop_patterns.txt"));
        ((GraphController) loader.getController()).drawGraph();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale localeEn = new Locale("en", "NZ");
        ResourceBundle bundle = ResourceBundle.getBundle("comp261/assig1/resources/strings", localeEn);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MapView.fxml"), bundle);
        Parent root  = (Parent) loader.load();

        primaryStage.setTitle(bundle.getString("title")); // set the title of the window from the bundle
        primaryStage.setScene(new Scene(root, 800, 700));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });

        graph = new Graph(new File("assig-1-maven/data/stops.txt"), new File("assig-1-maven/data/stop_patterns.txt"));
        ((GraphController) loader.getController()).drawGraph();
       
    }

    public static void main(String[] args) {
        launch(args);
    }

}
