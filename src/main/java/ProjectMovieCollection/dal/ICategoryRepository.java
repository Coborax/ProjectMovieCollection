package ProjectMovieCollection.dal;

import ProjectMovieCollection.be.Category;
import ProjectMovieCollection.utils.exception.CategoryDAOException;

import java.util.List;

public interface ICategoryRepository {

    /**
     * Creates category
     * @param category Category to be created
     * @return Category object
     * @throws CategoryDAOException
     */
    Category create(Category category) throws CategoryDAOException;

    /**
     * Deletes category
     * @param category Category to be deleted
     * @throws CategoryDAOException
     */
    void delete(Category category) throws CategoryDAOException;

    /**
     * Gets all categories
     * @return List of categories
     * @throws CategoryDAOException
     */
    List<Category> getAll() throws CategoryDAOException;

}
