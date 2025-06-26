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
    public ShoppingCart getUserCart(int userId) {
        ShoppingCart cart = new ShoppingCart();

        String sql = """
                SELECT p.*, sc.*
                FROM shopping_cart sc
                INNER JOIN products p ON sc.product_id = p.product_id
                WHERE sc.user_id = ?
                ORDER BY p.name
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = mapRow(resultSet);
                    int quantity = resultSet.getInt("quantity");

                    ShoppingCartItem item = new ShoppingCartItem();
                    item.setProduct(product);
                    item.setQuantity(quantity);
                    cart.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve shopping cart for user: " + userId, e);
        }

        return cart;
    }

    @Override
    public void insertCartItem(int userId, int productId) {
        String sql = """
                INSERT INTO\s
                shopping_cart (user_id, product_id, quantity)\s
                VALUES (?, ?, 1)
                ON DUPLICATE KEY UPDATE\s
                quantity = quantity + 1
               \s""";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, productId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Failed to add item to cart");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error adding product to cart: " + productId, e);
        }
    }

    @Override
    public void updateItemQuantity(int userId, int productId, int newQuantity) {
        String sql = """
                UPDATE
                shopping_cart\s
                SET quantity = ?\s
                WHERE user_id = ? AND product_id = ?
               \s""";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, newQuantity);
            statement.setInt(2, userId);
            statement.setInt(3, productId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Cart item not found for update");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update cart item quantity", e);
        }
    }

    @Override
    public void clearUserCart(int userId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear shopping cart for user: " + userId, e);
        }
    }


}

