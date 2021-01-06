/**
 *
 *
 * @author kjell
 */

package ProjectMovieCollection.gui.model;

import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.bll.CategoryManager;
import ProjectMovieCollection.bll.MovieManager;
import ProjectMovieCollection.utils.events.EventHandler;
import ProjectMovieCollection.utils.events.IMovieManagerListener;
import ProjectMovieCollection.utils.events.IMovieModelListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MovieBrowserModel extends EventHandler<IMovieModelListener> implements IMovieManagerListener {

    private MovieManager movieManager = new MovieManager();
    private CategoryManager categoryManager = new CategoryManager();

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

    private void loadMovies() {

    }

    @Override
    public void updateLoadProgress(float progress) {
        for (IMovieModelListener listener : getListeners()) {
            listener.updateLoadProgress(progress);
        }
    }

    public ObservableList<Movie> getObservableMovieList() {
        return FXCollections.observableList(movieManager.getAllMovies());
    }

    public ObservableList getObservableCategoryList() {
        return FXCollections.observableList(categoryManager.getAllCategories());
    }

}
