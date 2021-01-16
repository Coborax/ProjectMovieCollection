/**
 * @author kjell
 */

package ProjectMovieCollection.utils.exception;

/**
 * Gets thrown whenever theres issues with the database connection
 */
public class CategoryDAOException extends Exception{

    public CategoryDAOException(String message) {
        super(message);
    }

    public CategoryDAOException(String message, Exception error) {
        super(message, error);
    }
}
