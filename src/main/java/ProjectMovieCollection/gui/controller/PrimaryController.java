package ProjectMovieCollection.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ProjectMovieCollection.App;
import ProjectMovieCollection.bll.MovieData.IMovieInfoProvider;
import ProjectMovieCollection.bll.MovieData.MovieDBProvider;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PrimaryController implements Initializable {

    @FXML
    private ImageView moviePoster;
    @FXML
    private Label movieTitle;
    @FXML
    private TextArea movieDesc;
    @FXML
    private Label categories;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary",1280,720);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // The code beneath is only test code, it is to be removed
        int movieID = 508442;

        try {
            IMovieInfoProvider provider = new MovieDBProvider();
            moviePoster.setImage(new Image(provider.getMovieImage(movieID)));
            movieTitle.setText(provider.getMovieTitle(movieID));
            movieDesc.setText(provider.getMovieDesc(movieID));
            String cats = "Categories: ";

            for (String cat : provider.getCategories(movieID)) {
                cats += cat + ", ";
            }
            categories.setText(cats);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
