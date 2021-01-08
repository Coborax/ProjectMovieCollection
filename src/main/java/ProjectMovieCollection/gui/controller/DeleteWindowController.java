/**
 * @author kjell
 */

package ProjectMovieCollection.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class DeleteWindowController {

    @FXML
    private HBox hBox;

    public void deleteButton(ActionEvent actionEvent) {
        //TODO: Delete selected movie from all lists and database
    }

    public void cancelButton(ActionEvent actionEvent) {
        Stage stage = (Stage) hBox.getScene().getWindow();
        stage.close();
    }
}
