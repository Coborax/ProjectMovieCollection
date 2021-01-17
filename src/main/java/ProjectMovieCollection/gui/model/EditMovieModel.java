/**
 * @author kjell
 */

package ProjectMovieCollection.gui.model;

import ProjectMovieCollection.be.Movie;

import ProjectMovieCollection.bll.MovieManager;
import ProjectMovieCollection.utils.exception.MovieDAOException;

public class EditMovieModel {
    private MovieManager movieManager;
    private Movie movie;

    /**
     * Updates a movie with the given parameters
     * @param title The new title
     * @param description The new description
     * @param rating The new rating
     * @throws MovieDAOException If there is an error when calling the database
     */
    public void updateMovie(String title, String description, String rating) throws MovieDAOException {
        if (title != null) {
            movie.setTitle(title);
        }

        if (description != null) {
            movie.setDesc(description);
        }

        if (rating != null) {
            int ratingInt = Integer.parseInt(rating);
            movie.setRating(ratingInt);
        }

        movieManager.updateMovie(movie);
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setMovieManager(MovieManager movieManager) {
        this.movieManager = movieManager;
    }
}
