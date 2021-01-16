/**
 * @author kjell
 */

package ProjectMovieCollection.utils.exception;

/**
 * Gets thrown whenever the user wants to change an object without selecting something first.
 */
public class EmptySelectionException extends Exception{

    public EmptySelectionException(String message) {
        super(message);
    }

    public EmptySelectionException(String message, Exception error) {
        super(message, error);
    }
}
