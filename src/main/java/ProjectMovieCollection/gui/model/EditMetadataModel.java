package ProjectMovieCollection.gui.model;

import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.be.MovieSearchResult;
import ProjectMovieCollection.dal.MovieData.IMovieInfoProvider;
import ProjectMovieCollection.dal.MovieData.MovieDBProvider;
import ProjectMovieCollection.bll.MovieManager;
import ProjectMovieCollection.utils.exception.MovieDAOException;
import ProjectMovieCollection.utils.exception.MovieInfoException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;


public class EditMetadataModel {

    private ObservableList<MovieSearchResult> choices;
    private IMovieInfoProvider infoProvider;
    private MovieManager movieManager;
    private Movie movie;

    public EditMetadataModel() throws IOException {
        choices = FXCollections.observableArrayList();
        infoProvider = new MovieDBProvider();
    }

    /**
     * Searches for a movie in The Movie Database
     * @param term Term to be searched for
     * @throws MovieInfoException Error if The Movie Database is unavailable
     */
    public void search(String term) throws MovieInfoException {
        choices.clear();
        choices.addAll(infoProvider.search(term));
    }

    /**
     * Updates the movie locally and in the programs database
     * @param movieSearchResult The new movie
     * @throws MovieDAOException Connection to the database failed
     * @throws MovieInfoException Connection to The Movie Database failed
     */
    public void updateFromMovieResult(MovieSearchResult movieSearchResult) throws MovieDAOException, MovieInfoException {
        movie.setProviderID(movieSearchResult.getId());
        movie.setTitle(movieSearchResult.getName());
        try {
            movie.setDesc(infoProvider.getMovieDesc(movieSearchResult.getId()));
            movie.setImgPath(infoProvider.getMovieImage(movieSearchResult.getId()));
        } catch (MovieInfoException e) {
            throw new MovieInfoException("Could not load all the data");
        }

        movieManager.updateMovie(movie);
    }

    public ObservableList<MovieSearchResult> getObservableChoices() {
        return choices;
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
