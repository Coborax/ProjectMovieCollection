package ProjectMovieCollection.dal.dao.interfaces;

import ProjectMovieCollection.be.Category;
import ProjectMovieCollection.utils.exception.CategoryDAOException;

import java.util.List;

public interface CategoryRepository {

    Category create(Category category) throws CategoryDAOException;

    void delete(Category category) throws CategoryDAOException;

    List<Category> getAll() throws CategoryDAOException;

}
