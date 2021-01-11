package ProjectMovieCollection.gui.model;

import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.be.MovieSearchResult;
import ProjectMovieCollection.bll.MovieData.IMovieInfoProvider;
import ProjectMovieCollection.bll.MovieData.MovieDBProvider;
import ProjectMovieCollection.bll.MovieManager;
import ProjectMovieCollection.utils.exception.MovieDAOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditMetadataModel {

    private ObservableList<MovieSearchResult> choices;
    private IMovieInfoProvider infoProvider;
    private MovieManager movieManager;
    private Movie movie;

    public EditMetadataModel() throws IOException {
        choices = FXCollections.observableArrayList();
        infoProvider = new MovieDBProvider();
    }

    public void search(String term) {
        choices.clear();
        choices.addAll(infoProvider.search(term));
    }

    public void updateFromMovieResult(MovieSearchResult m) throws MovieDAOException {
        movie.setProviderID(m.getId());
        movie.setTitle(m.getName());
        movie.setDesc(infoProvider.getMovieDesc(m.getId()));
        movie.setImgPath(infoProvider.getMovieImage(m.getId()));

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

    public MovieManager getMovieManager() {
        return movieManager;
    }

    public void setMovieManager(MovieManager movieManager) {
        this.movieManager = movieManager;
    }
}
