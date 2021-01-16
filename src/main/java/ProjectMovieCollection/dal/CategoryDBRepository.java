/**
 * @author kjell
 */

package ProjectMovieCollection.dal;

import ProjectMovieCollection.be.Category;
import ProjectMovieCollection.utils.db.DBConnector;
import ProjectMovieCollection.utils.exception.CategoryDAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDBRepository implements ICategoryRepository {

    private DBConnector dbConnector;

    public CategoryDBRepository() {
        dbConnector = new DBConnector();
    }

    @Override
    public Category create(Category category) throws CategoryDAOException {
        try (Connection connection = dbConnector.getConnection()) {
            String sql = "INSERT INTO Categories (name) VALUES (?);";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, category.getName());
            statement.execute();

            try(ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return new Category(keys.getInt(1), category.getName());
                }
            }

            throw new CategoryDAOException("Could not create category");

        } catch (SQLException e) {
            throw new CategoryDAOException("Could not connect to database", e);
        }
    }

    @Override
    public void delete(Category category) throws CategoryDAOException {
        try (Connection connection = dbConnector.getConnection()) {
            String sql = "DELETE FROM Categories WHERE id=?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, category.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new CategoryDAOException("Could not connect to database", e);
        }
    }

    @Override
    public List<Category> getAll() throws CategoryDAOException {
        ArrayList<Category> allCategories = new ArrayList<>();

        try (Connection connection = dbConnector.getConnection()) {
            String sql = "SELECT * FROM Categories;";

            Statement statement = connection.createStatement();

            if (statement.execute(sql)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");

                    Category cat = new Category(id, name);
                    allCategories.add(cat);
                }
            }

        } catch (SQLException e) {
            throw new CategoryDAOException("Could not connect to database", e);
        }

        return allCategories;
    }

}
