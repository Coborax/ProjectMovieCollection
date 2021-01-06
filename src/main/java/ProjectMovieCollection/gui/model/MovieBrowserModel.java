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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class MovieBrowserModel extends EventHandler<IMovieModelListener> implements IMovieManagerListener {

    private MovieManager movieManager = new MovieManager();
    private CategoryManager categoryManager = new CategoryManager();

    private ObservableList<Movie> movieList;

    public MovieBrowserModel() {
        movieManager.addListener(this);
    }

    public void loadAllData() {
        Thread t = new Thread(() -> {
            movieManager.loadMoviesFromDisk();
            categoryManager.loadCategoriesFromMovieList(movieManager.getAllMovies());
            for (IMovieModelListener listener : getListeners()) {
                listener.dataFetched();
            }
        });
        t.start();
    }

    @Override
    public void updateLoadProgress(float progress) {
        for (IMovieModelListener listener : getListeners()) {
            listener.updateLoadProgress(progress);
        }
    }

    public String getCategoryString(Movie m) {
        String categoryString = "";
        for (Category c : m.getCategories()) {
            categoryString += c.getName() + ", ";
        }
        return categoryString;
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

}
