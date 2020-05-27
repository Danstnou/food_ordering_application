package com.example.myorder.model.repositories;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.myorder.model.database.CartDatabase;
import com.example.myorder.model.database.ICartDao;
import com.example.myorder.model.entities.ProductCart;
import com.example.myorder.utils.ExecutorServiceInstance;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class CartRepository {
    private CartDatabase cartDatabase;
    private ICartDao cartDao;

    private static ExecutorService executorService;

    private CartRepository(Application application) {
        cartDatabase = CartDatabase.getDatabase(application);
        cartDao = cartDatabase.cartDao();
        executorService = ExecutorServiceInstance.getInstance();
    }

    public static CartRepository instance;

    public static CartRepository getInstance(@Nullable Application application) {
        if (instance == null) {
            instance = new CartRepository(application);
        }
        return instance;
    }
    /*
     * Операции с корзиной
     */

    public void insert(ProductCart product) {
        executorService.execute(() -> cartDao.insert(product));
    }

    public void update(ProductCart product) {
        executorService.execute(() -> cartDao.update(product));
    }

    public void delete(ProductCart product) {
        executorService.execute(() -> cartDao.delete(product));
    }

    /*
     * Геттеры
     */

    public LiveData<List<ProductCart>> getAll() {
        return cartDao.getAll();
    }

    public LiveData<ProductCart> findProduct(String idProduct, String size) {
        return cartDao.findProduct(idProduct, size);
    }

    public LiveData<Integer> getTotalCostCart() {
        return cartDao.getTotalCostCart();
    }
}
