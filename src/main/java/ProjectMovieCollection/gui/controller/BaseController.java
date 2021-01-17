package ProjectMovieCollection.gui.controller;

import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.bll.MovieManager;

public class BaseController {

    private MovieManager movieManager;
    private Movie selectedMovie;

    /**
     * Gets the movie manager
     * @return Movie manager
     */
    public MovieManager getMovieManager() {
        return movieManager;
    }

    /**
     * Sets the movie manager
     * @param movieManager The movie manager to be set
     */
    public void setMovieManager(MovieManager movieManager) {
        this.movieManager = movieManager;
    }

    /**
     * Gets the selected movie
     * @return
     */
    public Movie getSelectedMovie() {
        return selectedMovie;
    }

    /**
     * Sets the selected movie
     * @param selectedMovie The selected movie
     */
    public void setSelectedMovie(Movie selectedMovie) {
        this.selectedMovie = selectedMovie;
    }
}



