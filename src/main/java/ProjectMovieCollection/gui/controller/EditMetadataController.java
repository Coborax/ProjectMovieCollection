/**
 * @author Mikkel
 */

package ProjectMovieCollection.gui.controller;

import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.be.MovieSearchResult;
import ProjectMovieCollection.bll.AlertManager;
import ProjectMovieCollection.bll.MovieManager;
import ProjectMovieCollection.gui.model.EditMetadataModel;
import ProjectMovieCollection.utils.exception.EmptySelectionException;
import ProjectMovieCollection.utils.exception.MovieDAOException;
import ProjectMovieCollection.utils.exception.MovieInfoException;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Edit Metadata window, changes a movies information depending on the selected movie in the list of searched movies.
 */
public class EditMetadataController extends BaseController implements Initializable {

    @FXML
    private JFXTextField movieID;
    @FXML
    private VBox VBox;
    @FXML
    private JFXListView<MovieSearchResult> relatedMovieList;

    private AlertManager alertManager;
    private EditMetadataModel editMetadataModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alertManager = new AlertManager();
        try {
            editMetadataModel = new EditMetadataModel();
        } catch (IOException e) {
            alertManager.displayError(e);
        }

        relatedMovieList.setItems(editMetadataModel.getObservableChoices());
    }

    /**
     * Closes the window
     * @param actionEvent
     */
    public void cancelButton(ActionEvent actionEvent) {
        Stage stage = (Stage) VBox.getScene().getWindow();
        stage.close();
    }

    /**
     * Updates the movies information and closes the window
     * @param actionEvent
     */
    public void confirmButton(ActionEvent actionEvent) {
        if (relatedMovieList.getSelectionModel().getSelectedItem() == null) {
            try {
                throw new EmptySelectionException("No movie selected");
            } catch (EmptySelectionException e) {
                alertManager.displayError("No movie selected", "Please select a movie before continuing!");
            }
        } else {
            try {
                // Updates the movie from the selected movie in relatedMovieList
                editMetadataModel.updateFromMovieResult(relatedMovieList.getSelectionModel().getSelectedItem());
            } catch (MovieInfoException e) {
                alertManager.displayError("Could not gather all data", "The Movie Database might be offline");
            } catch (MovieDAOException e) {
                alertManager.displayError("Could not connect to database", "Check your internet connection!");
            }
            Stage stage = (Stage) VBox.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Search button, searches for a movie from given textField.
     * @param actionEvent
     */
    public void searchForMovies(ActionEvent actionEvent) {
        try {
            editMetadataModel.search(movieID.getText());
        } catch (MovieInfoException e) {
            alertManager.displayError("Could not connect to The Movie Database","TMDB Might not be available right now");
        }
    }

    /**
     * Sets the movie manager for this controller
     * @param movieManager The movie manager to be set
     */
    @Override
    public void setMovieManager(MovieManager movieManager) {
        editMetadataModel.setMovieManager(movieManager);
        super.setMovieManager(movieManager);
    }

    /**
     * Sets the selected movie for this controller
     * @param selectedMovie The selected movie
     */
    @Override
    public void setSelectedMovie(Movie selectedMovie) {
        editMetadataModel.setMovie(selectedMovie);
        super.setSelectedMovie(selectedMovie);
    }

}
