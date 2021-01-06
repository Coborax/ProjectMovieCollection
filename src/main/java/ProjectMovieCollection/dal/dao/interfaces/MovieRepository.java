package ProjectMovieCollection.dal.dao.interfaces;

import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.utils.exception.MovieDAOException;

import java.util.List;

public interface MovieRepository {

    Movie create(Movie movie) throws MovieDAOException;

    void delete(Movie movie) throws MovieDAOException;

    void update(Movie movie) throws MovieDAOException;

    List<Movie> getAll() throws MovieDAOException;
}
