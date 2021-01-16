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

    public void search(String term) throws MovieInfoException {
        choices.clear();
        choices.addAll(infoProvider.search(term));
    }

    public void updateFromMovieResult(MovieSearchResult m) throws MovieDAOException, MovieInfoException {
        movie.setProviderID(m.getId());
        movie.setTitle(m.getName());
        try {
            movie.setDesc(infoProvider.getMovieDesc(m.getId()));
            movie.setImgPath(infoProvider.getMovieImage(m.getId()));
        } catch (MovieInfoException e) {
            throw new MovieInfoException("Could not connect to The Movie Database");
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
