package ProjectMovieCollection.bll.MovieData;

import java.io.File;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;

public class MovieDBProvider implements IMovieInfoProvider {

    private final String apiKey = "2088aeb2eeedcb6fb285b5bacf2863a5";

    @Override
    public File getMovieImage(int id) {
        TmdbMovies movies = new TmdbApi(apiKey).getMovies();
        MovieDb movie = movies.getMovie(id, "en");

        return null;
    }
}
