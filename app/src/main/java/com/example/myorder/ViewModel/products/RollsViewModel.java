package com.example.myorder.ViewModel.products;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.myorder.model.dto.Product;
import com.example.myorder.model.repositories.RollsRepository;

public class RollsViewModel extends BaseProductViewModel<Product> {
    public RollsViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void setProductRepository() {
        productRepository = RollsRepository.getInstance();
    }
}