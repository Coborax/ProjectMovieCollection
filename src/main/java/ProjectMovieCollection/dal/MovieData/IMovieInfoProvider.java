package ProjectMovieCollection.dal.MovieData;

import ProjectMovieCollection.be.MovieSearchResult;
import ProjectMovieCollection.utils.exception.MovieInfoException;

import java.util.List;

public interface IMovieInfoProvider {

    /**
     * Gets an image (Poster) of a specific movie
     * @param id The id of the movie for the provider
     * @return The path to the image poster
     */
    String getMovieImage(int id) throws MovieInfoException;

    /**
     * Gets the title of a specific movie
     * @param id The id of the movie for the provider
     * @return The title of the movie
     */
    String getMovieTitle(int id) throws MovieInfoException;

    /**
     * Gets the description of a specific movie
     * @param id The id of the movie for the provider
     * @return The description of the movie
     */
    String getMovieDesc(int id) throws MovieInfoException;

    /**
     * Gets a list of categories for the movie
     * @param id
     * @return A list of string for each category
     */
    List<String> getCategories(int id) throws MovieInfoException;

    /**
     * Searches for a movie based on the title
     * @param title The title of the movie
     * @return An ID of the movie, for the provider
     */
    int guessMovie(String title) throws MovieInfoException;

    /**
     * Searches for movies from a given search term
     * @param term The term to search for
     * @return A list containing MovieSearchResults
     * (These will contain information about the different movies that came up in the search)
     */
    List<MovieSearchResult> search(String term) throws MovieInfoException;

}

