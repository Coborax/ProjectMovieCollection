package ProjectMovieCollection.gui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ProjectMovieCollection.App;
import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.bll.MovieData.IMovieInfoProvider;
import ProjectMovieCollection.bll.MovieData.MovieDBProvider;
import ProjectMovieCollection.bll.MovieManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class PrimaryController implements Initializable {

    @FXML
    private ImageView moviePoster;
    @FXML
    private Label movieTitle;
    @FXML
    private TextArea movieDesc;
    @FXML
    private Label categories;

    @FXML
    private TextField movieDBID;

    @FXML
    private ListView<Movie> movieList;
    @FXML
    private ListView categoryList;

    //TEMP Just using movie manager here
    public MovieManager manager = new MovieManager();

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary",1280,720);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        manager.loadMoviesFromDisk();

        movieList.setItems(manager.getObservableMovieList());

        movieList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Movie>() {
            @Override
            public void changed(ObservableValue<? extends Movie> observable, Movie oldValue, Movie newValue) {
                updateUIToMovie(newValue);
            }
        });

        movieList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    try {
                        //TODO: Make this crossplatform??? ONLY WORKS ON WINDOWS
                        Runtime.getRuntime().exec("CMD /C START " + movieList.getSelectionModel().getSelectedItem().getFilepath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void updateUIToMovie(Movie m) {
        movieTitle.setText(m.getTitle());
        movieDesc.setText(m.getDesc());
        moviePoster.setImage(new Image(m.getImgPath()));
    }

    public void loadDataFromMovieDB(ActionEvent actionEvent) {
        manager.loadMovieInfo(movieList.getSelectionModel().getSelectedItem(), Integer.parseInt(movieDBID.getText()));
        updateUIToMovie(movieList.getSelectionModel().getSelectedItem());
    }
}
