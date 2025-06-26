package org.yearup.data.mysql;


import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.yearup.data.mysql.MySqlProductDao.mapRow;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart shoppingCart = new ShoppingCart();


        String sql = """
                SELECT\s
                p.*,
                sc.*
                FROM\s
                shopping_cart sc
                join products p on sc.product_id = p.product_id
                where sc.user_id = ?
               \s""";


        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setInt(1,userId);

            try(ResultSet results = preparedStatement.executeQuery()){
                while(results.next()){

                    Product product = mapRow(results);

                    ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
                    shoppingCartItem.setProduct(product);
                    shoppingCartItem.setQuantity(results.getInt("quantity"));
                    shoppingCart.add(shoppingCartItem);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return shoppingCart;
    }

    @Override
    public void addToCart(int userId, int productId) {
        String sql = """
                INSERT INTO
                shopping_cart (user_id, product_id)
                VALUES(?, ?)
                ON DUPLICATE KEY UPDATE quantity = quantity + 1;
                """;

        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        )
        {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, productId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //no return
    }

    @Override
    public void update(int userId, ShoppingCartItem shoppingCartItem) {

        String sql = """
                UPDATE shopping_cart
                SET quantity = ?
                WHERE user_id = ?
                AND product_id = ?
                """;
        try(Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        )
        {

            int quantity = shoppingCartItem.getQuantity();
            int productId = shoppingCartItem.getProductId();
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, productId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //no return
    }

    @Override
    public void delete(int userId) {
        // delete shopping cart
        String sql = """
                DELETE FROM shopping_cart
                WHERE user_id = ?
                """;

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //no return
    }
}
