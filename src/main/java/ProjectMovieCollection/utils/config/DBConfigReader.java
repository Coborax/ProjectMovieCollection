package ProjectMovieCollection.utils.config;

import java.io.IOException;

public class DBConfigReader extends ConfigReader {

    public DBConfigReader(String filename) throws IOException {
        super(filename);
    }

    public String getIP() {
        return getProperties().getProperty("ip");
    }

    public String getDBName() {
        return getProperties().getProperty("databaseName");
    }

    public String getUsername() {
        return getProperties().getProperty("username");
    }

    public String getPassword() {
        return getProperties().getProperty("password");
    }
}
