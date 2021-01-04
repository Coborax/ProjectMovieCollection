package ProjectMovieCollection.bll.MovieData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ProjectMovieCollection.utils.config.MovieDBConfig;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;

public class MovieDBProvider implements IMovieInfoProvider {

    private MovieDBConfig config;

    private final String imgBaseUrl = "http://image.tmdb.org/t/p/w500";

    public MovieDBProvider() throws IOException {
        config = new MovieDBConfig("moviedb.properties");
    }

    @Override
    public String getMovieImage(int id) {
        TmdbMovies movies = new TmdbApi(config.getAPIKey()).getMovies();
        MovieDb movie = movies.getMovie(id, "en");

        return imgBaseUrl + movie.getPosterPath();
    }

    @Override
    public String getMovieTitle(int id) {
        TmdbMovies movies = new TmdbApi(config.getAPIKey()).getMovies();
        MovieDb movie = movies.getMovie(id, "en");

        return movie.getTitle();
    }

    @Override
    public String getMovieDesc(int id) {
        TmdbMovies movies = new TmdbApi(config.getAPIKey()).getMovies();
        MovieDb movie = movies.getMovie(id, "en");

        return movie.getOverview();
    }

    @Override
    public List<String> getCategories(int id) {
        List<String> categories = new ArrayList<>();

        TmdbMovies movies = new TmdbApi(config.getAPIKey()).getMovies();
        MovieDb movie = movies.getMovie(id, "en");

        for (Genre genre : movie.getGenres()) {
            categories.add(genre.getName());
        }

        return categories;
    }

}