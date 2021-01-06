/**
 * @author kjell
 */

package ProjectMovieCollection.utils.exception;

public class CategoryDAOException extends Exception{
    public CategoryDAOException(String message) {
        super(message);
    }

    public CategoryDAOException(String message, Exception error) {
        super(message, error);
    }
}
