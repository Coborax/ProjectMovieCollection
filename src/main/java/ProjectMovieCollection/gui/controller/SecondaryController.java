package ProjectMovieCollection.gui.controller;

import java.io.IOException;

import ProjectMovieCollection.App;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary",1280,720);
    }
}