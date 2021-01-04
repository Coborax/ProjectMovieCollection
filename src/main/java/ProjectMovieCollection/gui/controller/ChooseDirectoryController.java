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

    @FXML
    private VBox vBox;
    @FXML
    private TextField directoryTextField;

    private final DirectoryChooser dirchoose = new DirectoryChooser();
    private final Directory dir = new Directory();

    public void browseButtonAction(ActionEvent actionEvent) {
        File file = dirchoose.showDialog(vBox.getScene().getWindow());
        dir.setFilepath(file);

        if (dir.getFilepath() != null) {
            System.out.println(dir.getFilepath());
            directoryTextField.setText(dir.getFilepath());
        }
    }

    public void confirmButtonAction(ActionEvent actionEvent) {
        try {
            if (dir.getFilepath() != null) {
                App.setRoot("view/primary", 1280, 720);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("No movie directory selected");
            alert.setContentText("Please choose a movie directory!");
            alert.showAndWait();
        }
    }
}
