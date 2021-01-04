package ProjectMovieCollection.bll.MovieData;

import java.io.File;
import java.io.IOException;

import ProjectMovieCollection.utils.config.MovieDBConfig;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;

public class MovieDBProvider implements IMovieInfoProvider {

    private MovieDBConfig config = new MovieDBConfig("moviedb.properties");

    public MovieDBProvider() throws IOException {

    }

    @Override
    public File getMovieImage(int id) {
        TmdbMovies movies = new TmdbApi(config.getAPIKey()).getMovies();
        MovieDb movie = movies.getMovie(id, "en");

        return null;
    }
}
