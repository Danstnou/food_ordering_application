package com.example.myorder.model.logic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.myorder.model.entities.ProductCart;
import com.example.myorder.model.repositories.CartRepository;

import java.util.List;

public class CartLogic implements ICartLogic {
    private CartRepository cartRepository;

    public CartLogic(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    /*
     * Реализация интерфейса: ICartLogic
     */

    public LiveData<List<ProductCart>> getAll() {
        return cartRepository.getAll();
    }

    public void increaseProduct(ProductCart productUser) {
        LiveData<ProductCart> productCartLiveData = cartRepository.findProduct(productUser.idProduct, productUser.size);
        productCartLiveData.observeForever(new Observer<ProductCart>() {
            @Override
            public void onChanged(ProductCart productDB) {
                productCartLiveData.removeObserver(this);

                if (productDB == null) {
                    cartRepository.insert(productUser);
                } else {
                    productDB.count = productDB.count + 1;
                    productDB.total_cost = (productDB.count * productDB.cost_one);
                    cartRepository.update(productDB);
                }
            }
        });
    }

    public void decreaseProduct(ProductCart productUser) {
        LiveData<ProductCart> productCartLiveData = cartRepository.findProduct(productUser.idProduct, productUser.size);
        productCartLiveData.observeForever(new Observer<ProductCart>() {
            @Override
            public void onChanged(ProductCart productBD) {
                productCartLiveData.removeObserver(this);

                if (productBD.count == 1) {
                    cartRepository.delete(productUser);
                } else {
                    productBD.count = productBD.count - 1;
                    productBD.total_cost = productBD.count * productBD.cost_one;
                    cartRepository.update(productBD);
                }
            }
        });
    }

    public LiveData<Integer> getTotalCostCart() {
        return cartRepository.getTotalCostCart();
    }
}