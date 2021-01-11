package ProjectMovieCollection.bll.MovieData;

import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.be.MovieSearchResult;

import java.io.File;
import java.util.List;

public interface IMovieInfoProvider {

    /**
     * Gets an image (Poster) of a specific movie
     * @param id The id of the movie for the provider
     * @return The path to the image poster
     */
    String getMovieImage(int id);

    /**
     * Gets the title of a specific movie
     * @param id The id of the movie for the provider
     * @return The title of the movie
     */
    String getMovieTitle(int id);

    /**
     * Gets the description of a specific movie
     * @param id The id of the movie for the provider
     * @return The description of the movie
     */
    String getMovieDesc(int id);

    /**
     * Gets a list of categories for the movie
     * @param id
     * @return A list of string for each category
     */
    List<String> getCategories(int id);

    /**
     * Searches for a movie based on the title
     * @param title The title of the movie
     * @return an ID of the movie, for the provider
     */
    int guessMovie(String title);

    List<MovieSearchResult> search(String term);

}
