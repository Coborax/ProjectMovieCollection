/**
 * @author kjell
 */

package ProjectMovieCollection.utils.exception;

/**
 * Gets thrown whenever the connection to the "The Movie Database" server was unsuccessful.
 */
public class MovieInfoException extends Exception{

    public MovieInfoException(String message) {
        super(message);
    }

    public MovieInfoException(String message, Exception error) {
        super(message, error);
    }
}
