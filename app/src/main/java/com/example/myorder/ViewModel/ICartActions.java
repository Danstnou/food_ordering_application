package com.example.myorder.ViewModel;

import com.example.myorder.model.entities.ProductCart;

public interface ICartActions {
    /*
     * Что может делать пользователь со своей корзиной
     */

    void increaseProduct(ProductCart product); // увеличить количество продуктов

    void decreaseProduct(ProductCart product); // уменьшить количество продуктов
}