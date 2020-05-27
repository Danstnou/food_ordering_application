package com.example.myorder.model.logic;

import androidx.lifecycle.LiveData;

import com.example.myorder.model.entities.ProductCart;

import java.util.List;

public interface ICartLogic {
    /*
     * Логика корзины продуктов
     */

    LiveData<List<ProductCart>> getAll(); // получить все продукты из БД

    void increaseProduct(ProductCart productCart); // увеличить количество продуктов

    void decreaseProduct(ProductCart productCart); // уменьшить количество продуктов

    LiveData<Integer> getTotalCostCart();
}