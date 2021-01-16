package ProjectMovieCollection.bll;

import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.bll.MovieData.IMovieInfoProvider;
import ProjectMovieCollection.bll.MovieData.MovieDBProvider;
import ProjectMovieCollection.dal.IMovieRepository;
import ProjectMovieCollection.dal.MovieDBRepository;
import ProjectMovieCollection.utils.events.EventHandler;
import ProjectMovieCollection.utils.events.IMovieManagerListener;
import ProjectMovieCollection.utils.exception.MovieDAOException;
import ProjectMovieCollection.utils.settings.Settings;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
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
            movieRepository = new MovieDBRepository();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method does three things:
     * 1. Add from persistent data storage - Add all movies from storage
     * 2. Add new movies - Go through all file on disk, and check for new movies
     * 3. Cleanup - Go through all movies, and check if it is missing on disk.
     * @throws MovieDAOException If there is an error with loading movies from persistent data storage
     */
    public void loadMovies() throws MovieDAOException {
        File dir = new File(Settings.DIRECTORY);
        int totalMovies = dir.listFiles().length;
        int loaded = 0;

        //Add all movies from storage
        movies.addAll(movieRepository.getAll());

        // Go through files in movie directory, and add new movies
        for (File file : dir.listFiles()) {
            if (FilenameUtils.getExtension(file.getPath()).equals("mp4") || FilenameUtils.getExtension(file.getPath()).equals("mkv")) {
                if (isNewMovie(file)) {
                    String filename = FilenameUtils.getName(file.getPath());
                    String movieName = filename.substring(0, filename.lastIndexOf("."));

                    Movie tempMovie = loadDataFromProvider(file, movieName);
                    Movie newMovie = movieRepository.create(tempMovie);

                    movies.add(newMovie);

                    loaded++;
                    for (IMovieManagerListener listener : getListeners()) {
                        listener.updateLoadProgress((float) loaded / (float) totalMovies);
                    }
                } else {
                    loaded++;
                }
            }
        }

        // Checks for and deletes movies in storage if the file is missing
        List<Movie> moviesToRemove = new ArrayList<>();
        for (Movie m : movies) {
            if (isFileMissing(m, dir.listFiles())) {
                movieRepository.delete(m);
                moviesToRemove.add(m);
            }
        }
        movies.removeAll(moviesToRemove);
    }

    /**
     * Checks if a movie file is missing
     * @param m The movie to check
     * @param files The list of all files
     * @return True if the file is missing, and false if it is not missing
     */
    private boolean isFileMissing(Movie m, File[] files) {
        for (File file : files) {
            if (m.getFilepath().equals(file.getPath())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if there already is an entry in the database for this file
     * @param file The file to check
     * @return True if the movie is not in the database, otherwise it returns false
     */
    private boolean isNewMovie(File file) {
        for (Movie m : movies) {
            if (m.getFilepath().equals(file.getPath())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Loads information about a movie, from a provider (Only to be used for temporary objects)
     * @param file The file of the movie
     * @param movieName The name of the movie
     * @return A movie with id -1, that has the data loaded from the provider
     */
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

    /**
     * Deletes a movie from both persistent data storage and disk
     * @param movie The movie to remove
     * @throws MovieDAOException If there is an error deleting on data storage
     * @throws IOException If there is an error deleting the file on disk
     */
    public void deleteMovie(Movie movie) throws MovieDAOException, IOException {
        movies.remove(movie);
        movieRepository.delete(movie);
        FileUtils.forceDelete(new File(movie.getFilepath()));
    }

    /**
     * Updates a movie on the data storage
     * @param m The movie to update
     * @throws MovieDAOException If there is an error updating the data storage
     */
    public void updateMovie(Movie m) throws MovieDAOException {
        movieRepository.update(m);
    }

    public List<Movie> getAllMovies() { return movies; }
}
