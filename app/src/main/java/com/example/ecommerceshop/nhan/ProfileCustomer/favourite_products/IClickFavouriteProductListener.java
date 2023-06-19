package com.example.ecommerceshop.nhan.ProfileCustomer.favourite_products;

import com.example.ecommerceshop.nhan.Model.Product;

public interface IClickFavouriteProductListener {
    void addProductToCart(Product product);
    void deleteProduct(Product product);
}
