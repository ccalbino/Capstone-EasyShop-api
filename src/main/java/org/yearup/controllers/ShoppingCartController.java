package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

// convert this class to a REST controller
// only logged in users should have access to these actions
@RestController
@RequestMapping("/cart")
@CrossOrigin
public class ShoppingCartController {

    private  ShoppingCartDao shoppingCartDao;
    private  UserDao userDao;
    private  ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ShoppingCart viewCart(Principal principal) {
        try {

            String username = principal.getName();
            int userId = userDao.getIdByUsername(username);
            return shoppingCartDao.getUserCart(userId);

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to retrieve shopping cart");
        }
    }

    @PostMapping("/products/{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ShoppingCart addProduct(@PathVariable int productId, Principal principal) {
        try {
            String username = principal.getName();
            int userId = userDao.getIdByUsername(username);

            shoppingCartDao.insertCartItem(userId, productId);
            return shoppingCartDao.getUserCart(userId);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to add product to cart");
        }
    }

    @PutMapping("/products/{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ShoppingCart updateQuantity(@PathVariable int productId, @RequestBody ShoppingCartItem cartItem, Principal principal) {
        try {


            String username = principal.getName();
            int userId = userDao.getIdByUsername(username);
            int newQuantity = cartItem.getQuantity();

            if (newQuantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative");
            }

            shoppingCartDao.updateItemQuantity(userId, productId, newQuantity);
            return shoppingCartDao.getUserCart(userId);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to update cart item");
        }
    }


    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ShoppingCart emptyCart(Principal principal) {
        try {
            String username = principal.getName();
            int userId = userDao.getIdByUsername(username);
            shoppingCartDao.clearUserCart(userId);
            return new ShoppingCart(); // Return empty cart

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to clear shopping cart");
        }
    }



}