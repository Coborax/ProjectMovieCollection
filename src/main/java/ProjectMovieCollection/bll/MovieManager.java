package ProjectMovieCollection.bll;

import ProjectMovieCollection.be.Category;
import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.bll.MovieData.interfaces.IMovieInfoProvider;
import ProjectMovieCollection.bll.MovieData.MovieDBProvider;
import ProjectMovieCollection.utils.events.EventHandler;
import ProjectMovieCollection.utils.events.IMovieManagerListener;
import ProjectMovieCollection.utils.settings.Settings;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieManager extends EventHandler<IMovieManagerListener> {

    private List<Movie> movies = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();

    private IMovieInfoProvider infoProvider;

    public MovieManager() {
        //TODO: Throw to UI
        try {
            infoProvider = new MovieDBProvider();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMoviesFromDisk() {
        File dir = new File(Settings.DIRECTORY);
        int totalMovies = dir.listFiles().length;
        int loaded = 0;

        for (File file : dir.listFiles()) {
            System.out.println(FilenameUtils.getExtension(file.getPath()));
            if (FilenameUtils.getExtension(file.getPath()).equals("mp4")) {
                int id = infoProvider.guessMovie(FilenameUtils.getName(file.getPath()).replace(".mp4", ""));
                Movie m = new Movie(-1, infoProvider.getMovieTitle(id), file.getPath());
                m.setDesc(infoProvider.getMovieDesc(id));
                m.setImgPath(infoProvider.getMovieImage(id));
                movies.add(m);
                loaded++;
                for (IMovieManagerListener listener : getListeners()) {
                    System.out.println(loaded / totalMovies);
                    listener.updateLoadProgress((float) loaded / (float) totalMovies);
                }
            }
        }
    }

    public void loadMovieInfo(Movie m, int id) {
        m.setTitle(infoProvider.getMovieTitle(id));
        m.setDesc(infoProvider.getMovieDesc(id));
        m.setImgPath(infoProvider.getMovieImage(id));
    }

    public List<Movie> getAllMovies() {
        return movies;
    }

}
