module jkm {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.microsoft.sqlserver.jdbc;
    requires java.sql;
    requires java.naming;
    requires themoviedbapi;
    requires org.slf4j;

    opens ProjectMovieCollection.gui.controller to javafx.fxml;
    opens ProjectMovieCollection to javafx.graphics;
    exports ProjectMovieCollection;
    exports ProjectMovieCollection.gui.controller;
}