package com.example.myorder.ViewModel.products;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.myorder.model.entities.ProductCart;
import com.example.myorder.model.logic.CartLogic;
import com.example.myorder.model.repositories.CartRepository;
import com.example.myorder.model.repositories.ProductRepository;
import com.example.myorder.utils.ExecutorServiceInstance;

import java.util.List;
import java.util.concurrent.ExecutorService;

public abstract class BaseProductViewModel<T> extends AndroidViewModel {
    protected ProductRepository productRepository;
    protected CartRepository cartRepository;
    protected CartLogic cartService;
    protected ExecutorService executorService;
    protected MutableLiveData<T> selectedProduct;

    public BaseProductViewModel(@NonNull Application application) {
        super(application);
        setProductRepository();
        cartRepository = CartRepository.getInstance(application);
        cartService = new CartLogic(CartRepository.getInstance(application));
        executorService = ExecutorServiceInstance.getInstance();
        selectedProduct = new MutableLiveData<>();
    }

    protected abstract void setProductRepository();

    public MutableLiveData<List<T>> getProductList() {
        return productRepository.getProductList();
    }

    public void increaseProduct(ProductCart productCart) {
        cartService.increaseProduct(productCart);
    }
}