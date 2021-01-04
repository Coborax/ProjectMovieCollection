/**
 * @author kjell
 */

package ProjectMovieCollection.gui.controller;

import java.io.File;

import ProjectMovieCollection.App;
import ProjectMovieCollection.bll.Directory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

public class ChooseDirectoryController {

    @FXML private VBox vBox;
    @FXML private TextField directoryTextField;

    private final DirectoryChooser dirchoose = new DirectoryChooser();
    private final Directory dir = new Directory();

    public void browseButtonAction(ActionEvent actionEvent) {
        // Opens the explorer and sets the selected directory as a file datatype.
        File file = dirchoose.showDialog(vBox.getScene().getWindow());

        // Update the selected directory path.
        dir.setFilepath(file);

        try {
            if (dir.getFilepath() != null) {
                // Sets the text field to the selected directory.
                directoryTextField.setText(dir.getFilepath());
            }
        } catch (Exception ignored) {}
    }

    public void confirmButtonAction(ActionEvent actionEvent) {
        try {
            if (dir.getFilepath() != null) {
                // Switch to the primary scene.
                App.setRoot("view/primary", 1280, 720);
            }
        } catch (Exception e) {
            // Open error prompt.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("No movie directory selected");
            alert.setContentText("Please choose a movie directory!");
            alert.showAndWait();
        }
    }
}
