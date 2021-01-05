package ProjectMovieCollection.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ProjectMovieCollection.App;
import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.bll.MovieManager;
import ProjectMovieCollection.utils.exception.MovieDirectoryException;
import com.jfoenix.controls.JFXTextArea;
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
    private JFXTextArea movieDesc;
    @FXML
    private Label categories;

    @FXML
    private ListView<Movie> movieList;
    @FXML
    private ListView categoryList;

    private Image posterPlaceholder;

    //TEMP Just using movie manager here
    private MovieManager manager = new MovieManager();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        posterPlaceholder = new Image("https://via.placeholder.com/300x600?text=Movie%20Poster");
        moviePoster.setImage(posterPlaceholder);

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
        try {
            moviePoster.setImage(new Image(m.getImgPath()));
        } catch (Exception e) {
            moviePoster.setImage(posterPlaceholder);
        }
    }

}
