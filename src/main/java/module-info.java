module jkm {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.microsoft.sqlserver.jdbc;
    requires java.sql;
    requires java.naming;

    opens ProjectMovieCollection to javafx.fxml;
    exports ProjectMovieCollection;
}