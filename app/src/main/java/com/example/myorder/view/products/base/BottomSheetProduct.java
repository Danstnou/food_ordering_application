package com.example.myorder.view.products.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.RequestManager;
import com.example.myorder.ViewModel.ICartActions;
import com.example.myorder.databinding.ProductBottomSheetBinding;
import com.example.myorder.model.dto.Product;
import com.example.myorder.model.entities.ProductCart;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class BottomSheetProduct extends BottomSheetDialogFragment {
    private ProductBottomSheetBinding binding;
    private ICartActions cartActions;
    private RequestManager requestManager;

    Product product;

    public BottomSheetProduct(RequestManager requestManager, ICartActions iCartActions) {
        this.requestManager = requestManager;
        this.cartActions = iCartActions;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ProductBottomSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonAdd.setOnClickListener(v -> {
            cartActions.increaseProduct(buildProductCart());
            Toast.makeText(getContext(), "Продукт добавлен", Toast.LENGTH_SHORT).show();
        });
        setupView();
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setupView() {
        requestManager
                .load(product.logo)
                .into(binding.logo);

        binding.textViewName.setText(product.name);
        binding.textViewIngredients.setText(product.ingredients);
        binding.textViewCost.setText(product.cost + "р.");
        binding.textViewWeight.setText(product.weight);
    }

    private ProductCart buildProductCart() {
        return new ProductCart.Builder()
                .withLogo(product.logo)
                .withName(product.name)
                .withIdProduct(product.idDocument)
                .withSize(product.weight)
                .withCost_one(product.cost)
                .build();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}