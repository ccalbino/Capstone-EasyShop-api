package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }
    @Override
    public List<Category> getAllCategories() throws SQLException {
        ArrayList<Category> allCategories = new ArrayList<>();
        String sql = """
                SELECT\s
                category_id
                ,name
                ,description
                FROM\s
                categories
               \s""";


        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                int categoryID = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String description = resultSet.getString(3);
                Category category = new Category(categoryID, name, description);
                allCategories.add(category);
            }
        }

        return allCategories;
    }

    @Override
    public Category getById(int categoryId)
    {
        String sql = """
                SELECT
                category_id
                ,name
                ,description
                FROM\s
                categories
                WHERE
                category_id = ?
               \s""";

        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ){
            preparedStatement.setInt(1, categoryId);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    int categoryID = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    String description = resultSet.getString(3);
                    return new Category(categoryID,name,description);
                }

            }

        }   catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // get category by id
        return null;



    }

    @Override
    public Category create(Category category)
    {
        String sql = """
                INSERT INTO
                categories (name, description)
                VALUES  (?, ?)
                """;

        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ){
            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());

            int rows = preparedStatement.executeUpdate();

            if(rows > 0){
                ResultSet results = preparedStatement.getGeneratedKeys();
                if(results.next()){
                    int id = results.getInt(1);

                    return getById(id);
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return null;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        String sql = """
                UPDATE
                name
                ,description
                FROM
                categories
                WHERE\s
                category_id = ?
               \s""";
        // update category
        try(Connection connection = getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, categoryId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void delete(int categoryId)
    {
        String sql = """
                DELETE FROM
                categories
                WHERE
                category_id = ? \s
               \s""";
        try(Connection connection = getConnection()){

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, categoryId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
