/**
 * @author kjell
 */

package ProjectMovieCollection.bll;

import javafx.scene.control.Alert;

public class AlertManager {

    public void displayAlertError(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}