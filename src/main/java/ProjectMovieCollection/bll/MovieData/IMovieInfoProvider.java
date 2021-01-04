package ProjectMovieCollection.bll.MovieData;

import ProjectMovieCollection.be.Movie;

import java.io.File;

public interface IMovieInfoProvider {

    String getMovieImage(int id);
    String getMovieTitle(int id);
    String getMovieDesc(int id);

}
