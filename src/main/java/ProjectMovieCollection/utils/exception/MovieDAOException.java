/**
 * @author kjell
 */

package ProjectMovieCollection.utils.exception;

public class MovieDAOException extends Exception{

    public MovieDAOException(String message) {
        super(message);
    }

    public MovieDAOException(String message, Exception error) {
        super(message, error);
    }
}
