/**
 * @author kjell
 */

package ProjectMovieCollection.gui.controller;

import java.io.File;
import java.io.IOException;
import ProjectMovieCollection.App;
import ProjectMovieCollection.bll.AlertManager;
import ProjectMovieCollection.gui.model.DirectoryModel;
import ProjectMovieCollection.utils.exception.MovieDirectoryException;
import ProjectMovieCollection.utils.settings.Settings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

public class ChooseDirectoryController {

    @FXML private VBox vBox;
    @FXML private TextField directoryTextField;

    private final DirectoryModel directoryModel = new DirectoryModel();
    private final AlertManager alertManager = new AlertManager();
    private final DirectoryChooser dir = new DirectoryChooser();

    /**
     * Browse directory button, changes the static variable DIRECTORY depending on the selected directory.
     * @param actionEvent
     */
    public void browseButton(ActionEvent actionEvent) {
        try {
            // Opens the explorer and sets the selected directory as a File datatype.
            File file = dir.showDialog(vBox.getScene().getWindow());
            // Update the DIRECTORY static variable.
            Settings.DIRECTORY = file.toString();
        } catch (NullPointerException ignored) {
            // Ignored because nothing should happen when directory selection is cancelled
        }

        if (Settings.DIRECTORY != null) {
            // Sets the text field to the selected directory.
            directoryTextField.setText(Settings.DIRECTORY);
        }
    }

    /**
     * Throws exception if confirmation failed, then displays an alert to inform the user of the error.
     * @param actionEvent
     */
    public void confirmButton(ActionEvent actionEvent) {
        try {
            directoryModel.confirm();
            App.setRoot("view/primary", 1280, 720);
        } catch (MovieDirectoryException e) {
            alertManager.displayError("No Directory Selected",
                    "Please select a directory before continuing.");

        } catch (IOException e) {
            alertManager.displayError("Could not connect to the database", "Check your internet connection!");
        }
    }
}
