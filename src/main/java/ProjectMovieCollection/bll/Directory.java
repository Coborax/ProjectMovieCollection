/**
 * @author kjell
 */

package ProjectMovieCollection.bll;

import java.io.File;

public class Directory {

    private File filepath;

    public String getFilepath() {
        return filepath.getAbsolutePath();
    }

    public void setFilepath(File filepath) {
        this.filepath = filepath;
    }

}
