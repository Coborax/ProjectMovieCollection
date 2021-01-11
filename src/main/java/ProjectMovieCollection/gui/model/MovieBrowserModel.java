/**
 *
 *
 * @author kjell
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
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MovieBrowserModel extends EventHandler<IMovieModelListener> implements IMovieManagerListener {

    private MovieManager movieManager;
    private CategoryManager categoryManager;

    private ObservableList<Movie> movieList;

    public MovieBrowserModel(MovieManager movieManager) throws CategoryDAOException {
        this.movieManager = movieManager;
        categoryManager = new CategoryManager();

        this.movieManager.addListener(this);
        movieList = FXCollections.observableArrayList();
    }

    public void loadAllData() {
        Thread t = new Thread(() -> {
            try {
                // Do the loading
                movieManager.loadMovies();
                categoryManager.loadCategoriesFromMovieList(movieManager.getAllMovies());
            } catch (MovieDAOException | CategoryDAOException e) {
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

    public String getCategoryString(Movie m) {
        String categoryString = "";
        for (Category c : m.getCategories()) {
            if (!c.getName().equals("All")) {
                categoryString += c.getName() + ", ";
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




    public void filterMovies(Category category) {
        List<Movie> movies = movieManager.getAllMovies();
        movieList.clear();

        for (Movie m : movies) {
            if (m.getCategories().contains(category) && !movieList.contains(m)) {
                movieList.add(m);
            }
        }
    }

    public void deleteMovie(Movie movie) throws MovieDAOException, IOException {
        movieManager.deleteMovie(movie);
        movieList.remove(movie);
    }

}
