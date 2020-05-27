package com.example.myorder.ViewModel.stocks;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.myorder.ViewModel.products.BaseProductViewModel;
import com.example.myorder.model.dto.Stock;
import com.example.myorder.model.repositories.StocksRepository;

public class StocksViewModel extends BaseProductViewModel<Stock> {
    public StocksViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void setProductRepository() {
        productRepository = StocksRepository.getInstance();
    }
}