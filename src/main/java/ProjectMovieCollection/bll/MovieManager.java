package ProjectMovieCollection.bll;

import ProjectMovieCollection.be.Category;
import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.bll.MovieData.IMovieInfoProvider;
import ProjectMovieCollection.bll.MovieData.MovieDBProvider;
import ProjectMovieCollection.utils.settings.Settings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieManager {

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
        for (File file : dir.listFiles()) {
            System.out.println(FilenameUtils.getExtension(file.getPath()));
            if (FilenameUtils.getExtension(file.getPath()).equals("mp4")) {
                movies.add(new Movie(file.getName(), file.getPath()));
            }
        }
    }

    public void loadMovieInfo(Movie m, int id) {
        m.setTitle(infoProvider.getMovieTitle(id));
        m.setDesc(infoProvider.getMovieDesc(id));
        m.setImgPath(infoProvider.getMovieImage(id));
    }

    public ObservableList getObservableMovieList() {
        return FXCollections.observableArrayList(movies);
    }

}
