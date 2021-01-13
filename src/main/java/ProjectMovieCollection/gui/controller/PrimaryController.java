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
        try {
            movieBrowserModel = new MovieBrowserModel(getMovieManager());
            alertManager = new AlertManager();
        } catch (CategoryDAOException e) {
            alertManager.displayError(e);
        }

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
                if (newValue != null) {
                    setSelectedMovie(newValue);
                    updateUIToMovie(newValue);

                    if (newValue.getRating() == -1) {
                        movieRating.setText("Rating: Not Rated");
                    } else {
                        movieRating.setText("Rating: " + newValue.getRating() + "/10");
                    }
                }
            }
        });

        movieList.getItems().addListener(new ListChangeListener<Movie>() {
            @Override
            public void onChanged(Change<? extends Movie> change) {
                movieList.setItems(movieBrowserModel.getObservableMovieList());
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

    private void updateUIToMovie(Movie m) throws RuntimeException {
        // Set movie title
        movieTitle.setText(m.getTitle());
        movieDesc.setText(m.getDesc());


        if (m.getRating() != -1 ) {
            movieRating.setText("Rating: " + m.getRating() + "/10");
        }

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
            alertManager.displayError("No movie selected", "Please select a movie!");
        }
    }

    public void openMetadataWindow(ActionEvent actionEvent) {
        showNewWindow("Edit Metadata","editMetadataWindow.fxml");
    }

    public void openEditWindow(ActionEvent actionEvent) {
        showNewWindow("Edit Movie","editMovieWindow.fxml");
    }

    public void deleteMovie(ActionEvent actionEvent) {
        if (movieList.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Delete Movie");
            alert.setHeaderText("You are about to delete \"" + movieList.getSelectionModel().getSelectedItem() + "\"");
            alert.setContentText("Are you sure you want to delete this movie?");
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

    @Override
    public void errorOccurred(Exception e) {
        alertManager.displayError(e);
    }

}
