/**
 * @author kjell
 */

package ProjectMovieCollection.gui.controller;

import java.io.File;
import java.io.IOException;
import ProjectMovieCollection.App;
import ProjectMovieCollection.utils.settings.Settings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

public class ChooseDirectoryController {

    @FXML private VBox vBox;
    @FXML private TextField directoryTextField;

    private final DirectoryChooser dir = new DirectoryChooser();

    public void browseButtonAction(ActionEvent actionEvent) {
        // Opens the explorer and sets the selected directory as a File datatype.
        File file = dir.showDialog(vBox.getScene().getWindow());
        // Update the DIRECTORY static variable.
        Settings.DIRECTORY = file.toString();

        if (Settings.DIRECTORY != null) {
            // Sets the text field to the selected directory.
            directoryTextField.setText(Settings.DIRECTORY);
        }
    }

    public void confirmButtonAction(ActionEvent actionEvent) throws IOException {
        if (Settings.DIRECTORY != null) {
            // Switch to the primary scene.
            App.setRoot("view/primary", 1280, 720);
        } else {
            // Open error prompt.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("No movie directory selected");
            alert.setContentText("Please choose a movie directory!");
            alert.showAndWait();
        }
    }
}
