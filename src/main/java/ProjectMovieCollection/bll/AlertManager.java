/**
 * @author kjell
 */

package ProjectMovieCollection.bll;

import javafx.scene.control.Alert;

public class AlertManager {

    /**
     * Displays an alert window, with a given header and message
     * @param header The header of the alert window
     * @param message The message of the window
     */
    public void displayError(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays an alert window, based on an exception
     * @param e The exception to make the alert window from
     */
    public void displayError(Exception e) {
        displayError("An error has occurred", e.getMessage());
    }
}
