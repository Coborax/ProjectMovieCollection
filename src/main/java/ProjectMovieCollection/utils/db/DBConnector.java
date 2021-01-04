package ProjectMovieCollection.utils.db;

import ProjectMovieCollection.utils.config.DBConfigReader;

import java.io.IOException;
import java.sql.Connection;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

public class DBConnector {
    private DBConfigReader config;
    private SQLServerDataSource dataSource;

    public DBConnector() {
        try {
            config = new DBConfigReader("db.properties");
            dataSource = new SQLServerDataSource();

            dataSource.setServerName(config.getIP());
            dataSource.setDatabaseName(config.getDBName());
            dataSource.setUser(config.getUsername());
            dataSource.setPassword(config.getPassword());
            dataSource.setPortNumber(1433);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public Connection getConnection() throws SQLServerException
    {
        return dataSource.getConnection();
    }
}
