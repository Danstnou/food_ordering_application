package com.example.myorder.ViewModel.products;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.myorder.model.dto.Product;
import com.example.myorder.model.repositories.DessertRepository;

public class DessertViewModel extends BaseProductViewModel<Product> {
    public DessertViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void setProductRepository() {
        productRepository = DessertRepository.getInstance();
    }
}