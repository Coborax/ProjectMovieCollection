/**
 * @author kjell
 */

package ProjectMovieCollection.gui.controller;

import ProjectMovieCollection.bll.MovieManager;
import ProjectMovieCollection.utils.exception.MovieDAOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class DeleteWindowController {

    @FXML
    private HBox hBox;

    private PrimaryController control = new PrimaryController();
    private MovieManager manager = new MovieManager();

    public void deleteButton(ActionEvent actionEvent) throws MovieDAOException {
        //TODO: Delete selected movie from all lists and database
        try {
            manager.deleteMovie(control.getSelectedItem());
        } catch (MovieDAOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //manager.loadMovies();
        Stage stage = (Stage) hBox.getScene().getWindow();
        stage.close();
    }

    public void cancelButton(ActionEvent actionEvent) {
        Stage stage = (Stage) hBox.getScene().getWindow();
        stage.close();
    }
}
