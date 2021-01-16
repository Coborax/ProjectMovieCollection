package ProjectMovieCollection.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import ProjectMovieCollection.App;
import ProjectMovieCollection.be.Category;
import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.bll.AlertManager;
import ProjectMovieCollection.bll.MovieManager;
import ProjectMovieCollection.gui.model.MovieBrowserModel;
import ProjectMovieCollection.utils.events.IMovieModelListener;
import ProjectMovieCollection.utils.exception.EmptySelectionException;
import ProjectMovieCollection.utils.exception.MovieDAOException;
import ProjectMovieCollection.utils.exception.CategoryDAOException;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextArea;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PrimaryController extends BaseController implements Initializable, IMovieModelListener {

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
    @FXML
    private Label movieRating;
    @FXML
    private TextField searchBox;

    private Image posterPlaceholder;

    private MovieBrowserModel movieBrowserModel;
    private AlertManager alertManager;

    public PrimaryController() {
        setMovieManager(new MovieManager());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alertManager = new AlertManager();
        try {
            movieBrowserModel = new MovieBrowserModel(getMovieManager());
        } catch (CategoryDAOException e) {
            alertManager.displayError(e);
        }

        // Hides the main content and shows a loading bar when the data is loading
        mainContent.setVisible(false);
        loader.setVisible(true);
        mainContent.managedProperty().bind(mainContent.visibleProperty());
        loader.managedProperty().bind(loader.visibleProperty());

        movieBrowserModel.addListener(this);
        movieBrowserModel.loadAllData();
        posterPlaceholder = moviePoster.getImage();

        // Listener that changes updates the movie UI (title, description and rating)
        movieList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Movie>() {
            @Override
            public void changed(ObservableValue<? extends Movie> observable, Movie oldValue, Movie newValue) {
                if (newValue != null) {
                    setSelectedMovie(newValue);
                    updateUIToMovie(newValue);

                    if (newValue.getRating() == -1) {
                        // If the movie has not been rated yet
                        movieRating.setText("Rating: Not Rated");
                    } else {
                        movieRating.setText("Rating: " + newValue.getRating() + "/10");
                    }
                }
            }
        });

        // Opens a movie if double clicked.
        movieList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    App.getHost().showDocument(movieList.getSelectionModel().getSelectedItem().getFilepath());
                }
            }
        });

        // Filters movies when a new category is selected
        categoryList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Category>() {
            @Override
            public void changed(ObservableValue<? extends Category> observableValue, Category category, Category t1) {
                movieBrowserModel.filterMovies(t1);
            }
        });

        movieList.getItems().addListener(new ListChangeListener<Movie>() {
            @Override
            public void onChanged(Change<? extends Movie> change) {
                movieList.setItems(movieBrowserModel.getObservableMovieList());
            }
        });
    }

    /**
     * Updates the UI depending on the movie selected
     * @param movie Movie to be changed
     */
    private void updateUIToMovie(Movie movie) {
        movieTitle.setText(movie.getTitle());
        movieDesc.setText(movie.getDesc());

        if (movie.getRating() != -1 ) {
            movieRating.setText("Rating: " + movie.getRating() + "/10");
        }

        // Shows the movies categories
        categories.setText(movieBrowserModel.getCategoryString(movie));

        // Shows the movies image from TMDB. If it can't find it, it's replaced by a placeholder.
        try {
            moviePoster.setImage(new Image(movie.getImgPath()));
        } catch (Exception e) {
            moviePoster.setImage(posterPlaceholder);
        }
    }

    /**
     * Sets the main content to visible again.
     */
    @Override
    public void dataFetched() {
        movieList.setItems(movieBrowserModel.getObservableMovieList());
        categoryList.setItems(movieBrowserModel.getObservableCategoryList());
        loader.setVisible(false);
        mainContent.setVisible(true);
    }

    /**
     * Sets the loading bars progression
     * @param progress The new progress value
     */
    @Override
    public void updateLoadProgress(float progress) {
        spinner.setProgress(progress);
    }

    /**
     * Opens a new window depending
     * @param title The windows title
     * @param fxml The windows fxml file
     */
    public void showNewWindow(String title, String fxml) {
        if (movieList.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/ProjectMovieCollection/view/" + fxml));

            Scene scene;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                alertManager.displayError("Could not open window", "Unable to open " + title + " window");
                return;
            }

            // Gets the controller from the BaseController class
            BaseController controller = fxmlLoader.getController();
            controller.setMovieManager(getMovieManager());
            controller.setSelectedMovie(getSelectedMovie());

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            updateUIToMovie(movieList.getSelectionModel().getSelectedItem());
            // Update title in the movies listview
            movieList.getSelectionModel().clearSelection();
            movieList.getSelectionModel().select(movieList.getSelectionModel().getSelectedItem());

        } else {
            try {
                throw new EmptySelectionException("No movie selected");
            } catch (EmptySelectionException e) {
                alertManager.displayError("No movie selected", "Please select a movie!");
            }
        }
    }

    public void openMetadataWindow(ActionEvent actionEvent) {
        showNewWindow("Edit Metadata","editMetadataWindow.fxml");
    }

    public void openEditWindow(ActionEvent actionEvent) {
        showNewWindow("Edit Movie","editMovieWindow.fxml");
    }

    /**
     * Deletes the selected movie, warns the user beforehand that it's irreversible
     * @param actionEvent
     */
    public void deleteMovie(ActionEvent actionEvent) {
        if (movieList.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Delete Movie");
            alert.setHeaderText("You are about to delete \"" + movieList.getSelectionModel().getSelectedItem() + "\"");
            alert.setContentText("Are you sure you want to delete this movie? This cannot be reverted");
            alert.initModality(Modality.APPLICATION_MODAL);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                try {
                    movieBrowserModel.deleteMovie(movieList.getSelectionModel().getSelectedItem());
                    movieList.setItems(movieBrowserModel.getObservableMovieList());
                } catch (MovieDAOException e) {
                    alertManager.displayError("Could not connect to database", "Check your internet connection");
                } catch (IOException e) {
                    alertManager.displayError("An Error Occurred", "Unable to delete movie" +
                            movieList.getSelectionModel().getSelectedItem());
                }
            } else {
                alert.close();
            }
        } else {
            alertManager.displayError("No movie selected","Please select a movie!");
        }
    }

    /**
     * Searches for a movie
     * @param actionEvent
     */
    public void searchMovie(ActionEvent actionEvent){
        String searchFilter = searchBox.getText().toLowerCase();

        ObservableList<Movie> allMovieList = FXCollections.observableArrayList(movieBrowserModel.getObservableMovieList());
        ObservableList<Movie> filterMovie = FXCollections.observableArrayList();

        for (Movie m : allMovieList) {
            if (m.getTitle().toLowerCase().contains(searchFilter)){
                filterMovie.add(m);
            }
        }
        movieList.setItems(filterMovie);

    }

    /**
     * Displays an error as an alert
     * @param e The error
     */
    @Override
    public void errorOccurred(Exception e) {
        alertManager.displayError(e);
    }

}
