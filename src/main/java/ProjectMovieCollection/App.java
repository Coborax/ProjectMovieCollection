package ProjectMovieCollection;

import ProjectMovieCollection.bll.MovieData.MovieDBProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("view/chooseDirectory"));
        stage.setScene(scene);
        stage.setTitle("Project Movie Collection");
        stage.centerOnScreen();
        stage.show();

        MovieDBProvider provider = new MovieDBProvider();
        provider.getMovieImage(508442);
    }

    public static void setRoot(String fxml, int width, int height) throws IOException {
        scene.setRoot(loadFXML(fxml));
        scene.getWindow().setWidth(width);
        scene.getWindow().setHeight(height);
        scene.getWindow().centerOnScreen();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}