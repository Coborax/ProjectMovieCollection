package ProjectMovieCollection.bll;

import ProjectMovieCollection.be.Category;
import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.bll.MovieData.IMovieInfoProvider;
import ProjectMovieCollection.bll.MovieData.MovieDBProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CategoryManager {

    private List<Category> categories = new ArrayList<>();
    private IMovieInfoProvider infoProvider;

    public CategoryManager() {
        //TODO: Throw to UI
        try {
            infoProvider = new MovieDBProvider();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createCategory(String name) {
        //TODO: Get from database
        categories.add(new Category(-1, -1, name));
    }

    public void loadCategoriesFromMovie(Movie m, int id) {
        for (String category : infoProvider.getCategories(id)) {
            for (Category c : categories) {
            }
        }
    }

    public List<Category> getAllCategories() { return categories; }

}
