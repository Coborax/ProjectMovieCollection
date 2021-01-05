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

    DirectoryModel dm = new DirectoryModel();
    AlertManager am = new AlertManager();

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


    /**
     * Throws exception if confirmation failed, then displays an alert to inform the user of the error.
     * @param actionEvent
     */
    public void confirmButtonAction(ActionEvent actionEvent) {
        try {
            dm.confirm();
            App.setRoot("view/primary", 1280, 720);
        } catch (MovieDirectoryException e) {
            am.displayAlertError("No Directory Selected","Please select a directory before continuing.");
        } catch (IOException e) {
            am.displayAlertError("Could not load next window", "Could not load next window");
            e.printStackTrace();
        }
    }
}
