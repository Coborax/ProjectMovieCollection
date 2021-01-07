package ProjectMovieCollection.bll;

import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.bll.MovieData.IMovieInfoProvider;
import ProjectMovieCollection.bll.MovieData.MovieDBProvider;
import ProjectMovieCollection.dal.IMovieRepository;
import ProjectMovieCollection.dal.MovieDAO;
import ProjectMovieCollection.utils.events.EventHandler;
import ProjectMovieCollection.utils.events.IMovieManagerListener;
import ProjectMovieCollection.utils.exception.MovieDAOException;
import ProjectMovieCollection.utils.settings.Settings;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MovieManager extends EventHandler<IMovieManagerListener> {

    private List<Movie> movies = new ArrayList<>();
    private IMovieInfoProvider infoProvider;
    private IMovieRepository movieRepository;

    public MovieManager() {
        //TODO: Throw to UI
        try {
            infoProvider = new MovieDBProvider();
            movieRepository = new MovieDAO();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMovies() throws MovieDAOException {
        File dir = new File(Settings.DIRECTORY);
        int totalMovies = dir.listFiles().length;
        int loaded = 0;

        movies.addAll(movieRepository.getAll());

        for (File file : dir.listFiles()) {
            System.out.println(FilenameUtils.getExtension(file.getPath()));
            if (FilenameUtils.getExtension(file.getPath()).equals("mp4") || FilenameUtils.getExtension(file.getPath()).equals("mkv")) {
                if (checkIfNewMovie(file)) {
                    String filename = FilenameUtils.getName(file.getPath());
                    String movieName = filename.substring(0, filename.lastIndexOf("."));

                    Movie tempMovie = loadDataFromProvider(file, movieName);
                    Movie newMovie = movieRepository.create(tempMovie);

                    movies.add(newMovie);

                    loaded++;
                    for (IMovieManagerListener listener : getListeners()) {
                        System.out.println(loaded / totalMovies);
                        listener.updateLoadProgress((float) loaded / (float) totalMovies);
                    }
                } else {
                    loaded++;
                }
            }
        }
    }

    private boolean checkIfNewMovie(File file) {
        for (Movie m : movies) {
            if (m.getFilepath().equals(file.getPath())) {
                return false;
            }
        }
        return true;
    }

    public Movie loadDataFromProvider(File file, String movieName) {
        int id = infoProvider.guessMovie(movieName);
        Movie m;

        if (id != -1) {
            m = new Movie(-1, infoProvider.getMovieTitle(id), file.getPath());
            m.setDesc(infoProvider.getMovieDesc(id));
            m.setImgPath(infoProvider.getMovieImage(id));
            m.setProviderID(id);
        } else {
            m = new Movie(-1, FilenameUtils.getName(file.getPath()), file.getPath());
        }
        return m;
    }

    public void loadMovieInfo(Movie m, int id) {
        m.setTitle(infoProvider.getMovieTitle(id));
        m.setDesc(infoProvider.getMovieDesc(id));
        m.setImgPath(infoProvider.getMovieImage(id));
    }

    public List<Movie> getAllMovies() { return movies; }

}
