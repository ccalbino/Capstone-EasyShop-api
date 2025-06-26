package org.yearup.data;

import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    ShoppingCart getUserCart(int userId);
    // add additional method signatures here
    void insertCartItem(int userId, int productId);
    void updateItemQuantity(int userId, int productId, int newQuantity);
    void clearUserCart(int userId);
}
