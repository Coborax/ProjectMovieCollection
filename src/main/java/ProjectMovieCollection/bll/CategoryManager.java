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

    public void loadCategoriesFromMovieList(List<Movie> movies) {
        for (Movie m : movies) {
            loadCategoriesFromMovie(m);
        }
    }

    private void loadCategoriesFromMovie(Movie m) {
        List<String> categoryStrings = infoProvider.getCategories(m.getProviderID());
        String allCategory = "All";

        categoryStrings.add(allCategory);
        categoryStrings.set(0, allCategory);

        for (String category : categoryStrings) {
            Category categoryToAdd = null;
            for (Category c : categories) {
                if (c.getName().equals(category)) {
                    categoryToAdd = c;
                }
            }
            if (categoryToAdd == null) {
                categoryToAdd = new Category(-1, category);
                categories.add(categoryToAdd);
            }
            m.addCategory(categoryToAdd);
        }
    }

    public List<Category> getAllCategories() { return categories; }

}
