package ProjectMovieCollection.utils.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class ConfigReader {

    private String filename;
    private InputStream inputStream;

    private Properties properties;

    public ConfigReader(String filename) throws IOException {
        this.filename = filename;
        loadConfig();
    }

    /**
     * Loads the config file
     * @throws IOException If there is a problem reading the file
     */
    private void loadConfig() throws IOException {
        try {
            properties = new Properties();
            String propFileName = filename;

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Properties getProperties() {
        return properties;
    }
}
