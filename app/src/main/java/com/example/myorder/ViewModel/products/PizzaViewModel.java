package com.example.myorder.ViewModel.products;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.myorder.model.dto.Pizza;
import com.example.myorder.model.repositories.PizzaRepository;

public class PizzaViewModel extends BaseProductViewModel<Pizza> {
    public PizzaViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void setProductRepository() {
        productRepository = PizzaRepository.getInstance();
    }
}
