module jkm {
    requires javafx.controls;
    requires javafx.fxml;

    opens ProjectMovieCollection to javafx.fxml;
    exports ProjectMovieCollection;
}