/**
 * @author kjell
 */

package ProjectMovieCollection.gui.controller;

import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.bll.AlertManager;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class EditMetadataController implements Initializable {

    @FXML
    private JFXTextField movieID;
    @FXML
    private VBox VBox;
    @FXML
    private JFXListView<Movie> relatedMovieList;

    private AlertManager am;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        am = new AlertManager();
    }

    public void cancelButton(ActionEvent actionEvent) {
        Stage stage = (Stage) VBox.getScene().getWindow();
        stage.close();
    }

    public void confirmButton(ActionEvent actionEvent) {
        //TODO: Implementation needed
        if (relatedMovieList.getSelectionModel().getSelectedItem() == null) {
            am.displayError("No Movie Selected", "Please select a movie");
        } else {
            Stage stage = (Stage) VBox.getScene().getWindow();
            stage.close();
        }

    }
}
