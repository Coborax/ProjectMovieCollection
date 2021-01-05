/**
 * @author kjell
 */

package ProjectMovieCollection.gui.model;

import ProjectMovieCollection.utils.exception.MovieDirectoryException;
import ProjectMovieCollection.utils.settings.Settings;

public class DirectoryModel {

    /**
     * Confirms whether DIRECTORY is empty, throws custom exception if it is
     * @throws MovieDirectoryException
     */
    public void confirm() throws MovieDirectoryException {
        if (Settings.DIRECTORY == null) {
            throw new MovieDirectoryException("No directory selected");
        }
    }

}
