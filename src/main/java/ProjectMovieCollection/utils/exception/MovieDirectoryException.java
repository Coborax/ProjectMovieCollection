/**
 * @author kjell
 */

package ProjectMovieCollection.utils.exception;

/**
 * Gets thrown whenever theres an issue with directory selection.
 */
public class MovieDirectoryException extends Exception {

    public MovieDirectoryException(String message) {
        super(message);
    }

    public MovieDirectoryException(String message, Exception err) {
        super(message, err);
    }
}
