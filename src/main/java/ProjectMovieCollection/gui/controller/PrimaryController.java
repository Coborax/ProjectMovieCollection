package ProjectMovieCollection.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import ProjectMovieCollection.App;
import ProjectMovieCollection.be.Category;
import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.bll.AlertManager;
import ProjectMovieCollection.gui.model.MovieBrowserModel;
import ProjectMovieCollection.utils.events.IMovieModelListener;
import ProjectMovieCollection.utils.exception.MovieDAOException;
import com.jfoenix.controls.*;
import ProjectMovieCollection.utils.exception.CategoryDAOException;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextArea;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    private MovieBrowserModel movieBrowserModel;
    private AlertManager am = new AlertManager();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            movieBrowserModel = new MovieBrowserModel();
        } catch (CategoryDAOException e) {
            am.displayError(e);
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
                    updateUIToMovie(newValue);
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

    public void showNewWindow(String title, String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/ProjectMovieCollection/view/" + fxml));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            am.displayError("Could not open window","Unable to open " + title + " window");
        }
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void openMetadataWindow(ActionEvent actionEvent) {
        showNewWindow("Edit Metadata","editMetadataWindow.fxml");
    }

    public void openEditWindow(ActionEvent actionEvent) {
        showNewWindow("Edit Movie","editWindow.fxml");
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
                    am.displayError("Could not connect to database", "Unable to connect to database");
                } catch (IOException e) {
                    am.displayError("An Error Occurred", "Unable to delete movie" + movieList.getSelectionModel().getSelectedItem());
                }
            } else {
                alert.close();
            }
        } else {
            am.displayError("No movie selected","Please select a movie before deleting it!");
        }
    }

    @Override
    public void errorOccurred(Exception e) {
        am.displayError(e);
    }

}
