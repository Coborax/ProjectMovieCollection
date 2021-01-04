package ProjectMovieCollection.utils.config;

import java.io.IOException;

public class MovieDBConfig extends ConfigReader{
    public MovieDBConfig(String filename) throws IOException {
        super(filename);
    }

    public String getAPIKey() { return getProperties().getProperty("api"); }
}
