/**
 * @author kjell
 */

package ProjectMovieCollection.gui.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class EditMetadataController {

    @FXML
    private JFXTextField movieID;
    @FXML
    private VBox VBox;


    public void confirmID(ActionEvent actionEvent) {
        //TODO: Implementation needed
        Stage stage = (Stage) VBox.getScene().getWindow();
        stage.close();
    }
}
