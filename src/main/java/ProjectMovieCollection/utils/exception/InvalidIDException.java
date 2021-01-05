/**
 * @author kjell
 */

package ProjectMovieCollection.utils.exception;

public class InvalidIDException extends Exception {

    public InvalidIDException(String message) {
        super(message);
    }

    public InvalidIDException(String message, Exception err) {
        super(message, err);
    }
}
