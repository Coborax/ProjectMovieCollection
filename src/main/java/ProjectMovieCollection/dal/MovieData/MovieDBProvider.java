package ProjectMovieCollection.dal.MovieData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ProjectMovieCollection.be.MovieSearchResult;
import ProjectMovieCollection.utils.config.MovieDBConfig;
import ProjectMovieCollection.utils.exception.MovieInfoException;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.tools.MovieDbException;

public class MovieDBProvider implements IMovieInfoProvider {

    private MovieDBConfig config;

    private final String imgBaseUrl = "http://image.tmdb.org/t/p/w500";

    public MovieDBProvider() throws IOException {
        config = new MovieDBConfig("moviedb.properties");
    }

    @Override
    public String getMovieImage(int id) throws MovieInfoException {
        TmdbMovies movies = new TmdbApi(config.getAPIKey()).getMovies();
        MovieDb movie = movies.getMovie(id, "en");

        if (movie.getPosterPath() == null) {
            throw new MovieInfoException("Could not get movie poster from https://www.themoviedb.org/");
        }

        return imgBaseUrl + movie.getPosterPath();
    }

    @Override
    public String getMovieTitle(int id) throws MovieInfoException {
        TmdbMovies movies = new TmdbApi(config.getAPIKey()).getMovies();
        MovieDb movie = movies.getMovie(id, "en");
        String movieTitle = movie.getTitle();

        if (movieTitle == null || movieTitle.isEmpty()) {
            throw new MovieInfoException("Could not load movie title from https://www.themoviedb.org/");
        }

        return movieTitle;
    }

    @Override
    public String getMovieDesc(int id) throws MovieInfoException {
        TmdbMovies movies = new TmdbApi(config.getAPIKey()).getMovies();
        MovieDb movie = movies.getMovie(id, "en");
        String movieDesc = movie.getOverview();

        if (movieDesc == null || movieDesc.isEmpty()) {
            throw new MovieInfoException("Could not load movie description from https://www.themoviedb.org/");
        }

        return movieDesc;
    }

    @Override
    public List<String> getCategories(int id) throws MovieInfoException {
        List<String> categories = new ArrayList<>();

        TmdbMovies movies = new TmdbApi(config.getAPIKey()).getMovies();
        MovieDb movie = movies.getMovie(id, "en");

        for (Genre genre : movie.getGenres()) {
            categories.add(genre.getName());
        }

        if (categories.size() <= 0) {
            throw new MovieInfoException("Could not get movie categories from https://www.themoviedb.org/");
        }

        return categories;
    }

    public int guessMovie(String title) throws MovieInfoException {
        TmdbSearch search = new TmdbApi(config.getAPIKey()).getSearch();
        MovieResultsPage resultsPage = search.searchMovie(title, 0, null, true, 0);

        if (resultsPage.getResults().size() != 0) {
            MovieDb movie = resultsPage.getResults().get(0);
            return movie.getId();
        }
        throw new MovieDbException("Could not guess a movie");
    }

    public List<MovieSearchResult> search(String term) throws MovieInfoException {
        List<MovieSearchResult> result = new ArrayList<>();

        TmdbSearch search = new TmdbApi(config.getAPIKey()).getSearch();
        MovieResultsPage resultsPage = search.searchMovie(term, 0, null, true, 0);

        if (resultsPage.getResults().size() != 0) {
            for (MovieDb movieDb : resultsPage.getResults()) {
                result.add(new MovieSearchResult(movieDb.getId(), movieDb.getTitle()));
            }
        }

        if (result.size() <= 0) {
            throw new MovieInfoException("Could not get search result from https://www.themoviedb.org/");
        }

        return result;
    }

}
