package ProjectMovieCollection.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import ProjectMovieCollection.App;
import ProjectMovieCollection.be.Category;
import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.gui.model.MovieBrowserModel;
import ProjectMovieCollection.utils.events.IMovieModelListener;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextArea;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PrimaryController implements Initializable, IMovieModelListener {

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
    private ListView<Category> categoryList;
    @FXML
    private HBox mainContent;
    @FXML
    private VBox loader;
    @FXML
    private JFXSpinner spinner;

    private Image posterPlaceholder;
    private MovieBrowserModel movieBrowserModel = new MovieBrowserModel();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainContent.setVisible(false);
        loader.setVisible(true);

        mainContent.managedProperty().bind(mainContent.visibleProperty());
        loader.managedProperty().bind(loader.visibleProperty());

        movieBrowserModel.addListener(this);
        movieBrowserModel.loadAllData();

        posterPlaceholder = moviePoster.getImage();
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
                    App.getHost().showDocument(movieList.getSelectionModel().getSelectedItem().getFilepath());
                }
            }
        });

        categoryList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Category>() {
            @Override
            public void changed(ObservableValue<? extends Category> observableValue, Category category, Category t1) {
                movieBrowserModel.filterMovies(t1);
            }
        });
    }

    private void updateUIToMovie(Movie m) {
        movieTitle.setText(m.getTitle());
        movieDesc.setText(m.getDesc());
        categories.setText(movieBrowserModel.getCategoryString(m));
        try {
            moviePoster.setImage(new Image(m.getImgPath()));
        } catch (Exception e) {
            moviePoster.setImage(posterPlaceholder);
        }
    }

    @Override
    public void dataFetched() {
        movieList.setItems(movieBrowserModel.getObservableMovieList());
        categoryList.setItems(movieBrowserModel.getObservableCategoryList());
        loader.setVisible(false);
        mainContent.setVisible(true);
    }

    @Override
    public void updateLoadProgress(float progress) {
        spinner.setProgress(progress);
    }
}
