/**
 * @author kjell
 * @autor Mikkel
 */

package ProjectMovieCollection.gui.model;

import ProjectMovieCollection.be.Category;
import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.bll.CategoryManager;
import ProjectMovieCollection.bll.MovieManager;
import ProjectMovieCollection.utils.events.EventHandler;
import ProjectMovieCollection.utils.events.IMovieManagerListener;
import ProjectMovieCollection.utils.events.IMovieModelListener;
import ProjectMovieCollection.utils.exception.CategoryDAOException;
import ProjectMovieCollection.utils.exception.MovieDAOException;
import ProjectMovieCollection.utils.exception.MovieInfoException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public class MovieBrowserModel extends EventHandler<IMovieModelListener> implements IMovieManagerListener {

    private final MovieManager movieManager;
    private final CategoryManager categoryManager;

    private ObservableList<Movie> movieList;

    public MovieBrowserModel(MovieManager movieManager) throws CategoryDAOException {
        this.movieManager = movieManager;
        categoryManager = new CategoryManager();

        this.movieManager.addListener(this);
        movieList = FXCollections.observableArrayList();
    }

    /**
     * Loads data on a new thread
     */
    public void loadAllData() {
        Thread t = new Thread(() -> {
            try {
                // Do the loading
                movieManager.loadMovies();
                categoryManager.loadCategoriesFromMovieList(movieManager.getAllMovies());
            } catch (MovieDAOException | CategoryDAOException | MovieInfoException e) {
                // Notify listeners that an error has occurred
                Platform.runLater(() -> {
                    for (IMovieModelListener listener : getListeners()) {
                        listener.errorOccurred(e);
                    }
                });
            }

            // Tell listeners that the data has been fetched
            Platform.runLater(() -> {
                for (IMovieModelListener listener : getListeners()) {
                    listener.dataFetched();
                }
            });
        });
        t.start();
    }

    @Override
    public void updateLoadProgress(float progress) {
        Platform.runLater(() -> {
            for (IMovieModelListener listener : getListeners()) {
                listener.updateLoadProgress(progress);
            }
        });
    }

    /**
     * Gets a string with categories for a movie
     * @param movie movie of which you want the categories from
     * @return String with categories
     */
    public String getCategoryString(Movie movie) {
        String categoryString = "";
        for (Category category : movie.getCategories()) {
            if (!category.getName().equals("All")) {
                categoryString += category.getName() + ", ";
            }
        }
        return categoryString.substring(0, categoryString.lastIndexOf(","));
    }

    public ObservableList<Movie> getObservableMovieList() {
        return movieList;
    }

    public ObservableList<Category> getObservableCategoryList() {
        return FXCollections.observableList(categoryManager.getAllCategories());
    }

    /**
     * Filters movies with a given category and adds them to a list of movies
     * @param category The category to be filtered
     */
    public void filterMovies(Category category) {
        List<Movie> movies = movieManager.getAllMovies();
        movieList.clear();

        for (Movie m : movies) {
            if (m.getCategories().contains(category) && !movieList.contains(m)) {
                movieList.add(m);
            }
        }
    }

    /**
     * Deletes a movie
     * @param movie The movie to be deleted
     * @throws MovieDAOException When connection to the database failed
     * @throws IOException If it cannot find the file
     */
    public void deleteMovie(Movie movie) throws MovieDAOException, IOException {
        movieManager.deleteMovie(movie);
        movieList.remove(movie);
    }

}
