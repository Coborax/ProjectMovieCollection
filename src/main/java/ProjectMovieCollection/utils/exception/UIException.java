/**
 * @author kjell
 */

package ProjectMovieCollection.utils.exception;

import javafx.scene.control.Alert;

public class UIException extends Exception {

    public UIException(String message) {
        super(message);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("An Error Occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
