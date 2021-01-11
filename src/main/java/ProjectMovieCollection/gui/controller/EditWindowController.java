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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
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

        titleTextField.setText(getSelectedMovie().getTitle());
        //descriptionTextArea.setText(getSelectedMovie().getDesc());

        /*if (getSelectedMovie().getRating() != -1) {
            ratingBox.setPromptText(String.valueOf(getSelectedMovie().getRating()));
        } else {
            ratingBox.setPromptText("Not rated");
        }*/

        ratingBox.getItems().addListener(new ListChangeListener<Label>() {
            @Override
            public void onChanged(Change<? extends Label> change) {
                if (change.toString() != null) {
                    rating = change.toString();
                }
            }
        });

        titleTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                title = t1;
            }
        });

        descriptionTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                description = t1;
            }
        });
    }

    @Override
    public void setMovieManager(MovieManager movieManager) {
        editMovieModel.setMovieManager(movieManager);
        super.setMovieManager(movieManager);
    }

    @Override
    public void setSelectedMovie(Movie selectedMovie) {
        editMovieModel.setMovie(selectedMovie);
        super.setSelectedMovie(selectedMovie);
    }


    public void confirmButton(ActionEvent actionEvent) {
        try {
            editMovieModel.updateMovie(title, description, rating);
        } catch (MovieDAOException e) {
            alertManager.displayError("Could not connect to database", "Check your internet connection!");
        }

        Stage stage = (Stage) VBox.getScene().getWindow();
        stage.close();
    }
}
