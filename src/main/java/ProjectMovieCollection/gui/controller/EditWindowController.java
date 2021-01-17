/**
 * @author kjell
 */

package ProjectMovieCollection.gui.controller;

import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.bll.AlertManager;
import ProjectMovieCollection.bll.MovieManager;
import ProjectMovieCollection.gui.model.EditMovieModel;
import ProjectMovieCollection.utils.exception.MovieDAOException;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditWindowController extends BaseController implements Initializable {

    @FXML
    public JFXTextArea descriptionTextArea;
    @FXML
    public JFXTextField titleTextField;
    @FXML
    public VBox VBox;
    @FXML
    private JFXComboBox<Label> ratingBox;

    private final List<Integer> ratingList = new ArrayList<>();
    private String rating = null;
    private String title = null;
    private String description = null;

    private final EditMovieModel editMovieModel = new EditMovieModel();
    private final AlertManager alertManager = new AlertManager();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Add 10 integers to list
        for (int i = 1; i < 11; i++) {
            ratingList.add(i);
        }

        // Use list as combobox content
        for (int i = 0; i < 10; i++) {
            ratingBox.getItems().add(new Label(ratingList.get(i).toString()));
        }

    }

    /**
     * Updates the title, description and rating of a movie if changed. Closes the window
     * @param actionEvent
     */
    public void confirmButton(ActionEvent actionEvent) {
        title = titleTextField.getText();
        description = descriptionTextArea.getText();
        if (ratingBox.getValue() != null) {
            rating = ratingBox.getValue().getText();
        }

        try {
            editMovieModel.updateMovie(title, description, rating);
        } catch (MovieDAOException e) {
            alertManager.displayError("Could not connect to database", "Check your internet connection!");
        }

        Stage stage = (Stage) VBox.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets the movie manager for this controller
     * @param movieManager The movie manager to be set
     */
    @Override
    public void setMovieManager(MovieManager movieManager) {
        editMovieModel.setMovieManager(movieManager);
        super.setMovieManager(movieManager);
    }

    /**
     * Sets the selected movie for this controller
     * @param selectedMovie The selected movie
     */
    @Override
    public void setSelectedMovie(Movie selectedMovie) {
        // Sets the title and description in the edit window from the selected movie
        titleTextField.setText(selectedMovie.getTitle());
        descriptionTextArea.setText(selectedMovie.getDesc());

        // Sets the rating in the combobox as prompt text, if the rating is -1 it will be shown as not rated
        if (selectedMovie.getRating() != -1) {
            ratingBox.setPromptText(String.valueOf(selectedMovie.getRating()));
        } else {
            ratingBox.setPromptText("Not rated");
        }

        editMovieModel.setMovie(selectedMovie);
        super.setSelectedMovie(selectedMovie);
    }

}
