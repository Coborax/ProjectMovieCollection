/**
 *
 *
 * @author kjell
 */

package ProjectMovieCollection.gui.model;

import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.bll.MovieManager;
import ProjectMovieCollection.utils.events.EventHandler;
import ProjectMovieCollection.utils.events.IMovieManagerListener;
import ProjectMovieCollection.utils.events.IMovieModelListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MovieModel extends EventHandler<IMovieModelListener> implements IMovieManagerListener {

    private MovieManager manager = new MovieManager();

    public MovieModel() {
        manager.addListener(this);
    }

    public void loadMovies() {
        Thread t = new Thread(() -> {
            manager.loadMoviesFromDisk();
            for (IMovieModelListener listener : getListeners()) {
                listener.dataFetched();
            }
        });
        t.start();
    }

    public ObservableList<Movie> getObservableMovieList() {
        return FXCollections.observableList(manager.getAllMovies());
    }

    @Override
    public void updateLoadProgress(float progress) {
        for (IMovieModelListener listener : getListeners()) {
            listener.updateLoadProgress(progress);
        }
    }
}
