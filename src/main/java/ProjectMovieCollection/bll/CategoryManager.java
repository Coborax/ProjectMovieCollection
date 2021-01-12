package ProjectMovieCollection.bll;

import ProjectMovieCollection.be.Category;
import ProjectMovieCollection.be.Movie;
import ProjectMovieCollection.bll.MovieData.IMovieInfoProvider;
import ProjectMovieCollection.bll.MovieData.MovieDBProvider;
import ProjectMovieCollection.dal.CategoryDAO;
import ProjectMovieCollection.dal.ICategoryRepository;
import ProjectMovieCollection.dal.IMovieRepository;
import ProjectMovieCollection.dal.MovieDAO;
import ProjectMovieCollection.utils.exception.CategoryDAOException;
import ProjectMovieCollection.utils.exception.MovieDAOException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryManager {

    private List<Category> categories = new ArrayList<>();
    private IMovieInfoProvider infoProvider;
    private ICategoryRepository categoryRepository;
    private IMovieRepository movieRepository;

    public CategoryManager() throws CategoryDAOException {
        //TODO: Throw to UI
        try {
            infoProvider = new MovieDBProvider();
            categoryRepository = new CategoryDAO();
            movieRepository = new MovieDAO();
        } catch (IOException e) {
            e.printStackTrace();
        }

        categories.addAll(categoryRepository.getAll());
        Category allCategory = new Category(-1, "All");
        categories.add(0, allCategory);
    }

    public void loadCategoriesFromMovieList(List<Movie> movies) throws CategoryDAOException, MovieDAOException {
        for (Movie m : movies) {
            if (m.getCategories().size() == 0) {
                loadCategoriesFromMovie(m);
            }
        }
    }

    private void loadCategoriesFromMovie(Movie m) throws CategoryDAOException, MovieDAOException {
        List<String> categoryStrings = new ArrayList<>();
        m.addCategory(categories.get(0));

        if (m.getProviderID() != -1) {
            categoryStrings.addAll(infoProvider.getCategories(m.getProviderID()));
        }

        for (String category : categoryStrings) {
            Category categoryToAdd = null;
            for (Category c : categories) {
                if (c.getName().equals(category)) {
                    categoryToAdd = c;
                }
            }
            if (categoryToAdd == null) {
                Category tempCategory = new Category(-1, category);
                categoryToAdd = categoryRepository.create(tempCategory);
                categories.add(categoryToAdd);
            }

            movieRepository.addCategoryToMovie(m, categoryToAdd);
            m.addCategory(categoryToAdd);
        }
    }

    public List<Category> getAllCategories() {
        return categories;
    }

}
