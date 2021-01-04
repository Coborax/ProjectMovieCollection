package ProjectMovieCollection.gui.controller;

import java.io.IOException;

import ProjectMovieCollection.App;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary",1280,720);
    }
}
