package com.example.myorder.ViewModel.cart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myorder.model.entities.ProductCart;
import com.example.myorder.model.logic.CartLogic;
import com.example.myorder.model.repositories.CartRepository;
import com.example.myorder.utils.ExecutorServiceInstance;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class CartViewModel extends AndroidViewModel {
    private CartLogic cartLogic;
    private ExecutorService executorService;

    public CartViewModel(@NonNull Application application) {
        super(application);
        cartLogic = new CartLogic(CartRepository.getInstance(application));
        executorService = ExecutorServiceInstance.getInstance();
    }

    /*
     * Операции корзины
     */

    public LiveData<List<ProductCart>> getAll() {
        return cartLogic.getAll();
    }

    public void increaseProduct(ProductCart productCart) {
        cartLogic.increaseProduct(productCart);
    }

    public void decreaseProduct(ProductCart productCart) {
        cartLogic.decreaseProduct(productCart);
    }

    public LiveData<Integer> getTotalCost() {
        return cartLogic.getTotalCostCart();
    }
}