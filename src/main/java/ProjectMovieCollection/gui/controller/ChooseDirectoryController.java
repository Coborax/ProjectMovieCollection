/**
 * @author kjell
 */

package ProjectMovieCollection.gui.controller;

import java.io.File;
import ProjectMovieCollection.App;
import ProjectMovieCollection.utils.exception.UIException;
import ProjectMovieCollection.utils.settings.Settings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

public class ChooseDirectoryController {

    @FXML private VBox vBox;
    @FXML private TextField directoryTextField;

    private final DirectoryChooser dir = new DirectoryChooser();

    public void browseButtonAction(ActionEvent actionEvent) {
        try {
            // Opens the explorer and sets the selected directory as a File datatype.
            File file = dir.showDialog(vBox.getScene().getWindow());
            // Update the DIRECTORY static variable.
            Settings.DIRECTORY = file.toString();
        } catch (NullPointerException e) {
            System.out.println("Cancelled directory selection ");
        }

        if (Settings.DIRECTORY != null) {
            // Sets the text field to the selected directory.
            directoryTextField.setText(Settings.DIRECTORY);
        }
    }

    public void confirmButtonAction(ActionEvent actionEvent) {
        try {
            if (Settings.DIRECTORY != null) {
                // Switch to the primary scene.
                App.setRoot("view/primary", 1280, 720);
            } else {throw new UIException("No Directory Selected");}
        } catch (Exception e) {
            System.out.println("No directory selected");
        }

    }
}
