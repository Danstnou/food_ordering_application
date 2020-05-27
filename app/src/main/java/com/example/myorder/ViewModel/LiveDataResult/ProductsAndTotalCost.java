package com.example.myorder.ViewModel.LiveDataResult;

import com.example.myorder.model.entities.ProductCart;

import java.util.List;

public class ProductsAndTotalCost {
    public List<ProductCart> productCartList;
    public int totalCost = -1;

    public ProductsAndTotalCost() {
    }

    public boolean isComplete() {
        return (productCartList != null && totalCost != -1);
    }
}
