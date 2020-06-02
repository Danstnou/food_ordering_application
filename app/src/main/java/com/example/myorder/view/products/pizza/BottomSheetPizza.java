package com.example.myorder.view.products.pizza;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.RequestManager;
import com.example.myorder.ViewModel.ICartActions;
import com.example.myorder.databinding.PizzaBottomSheetBinding;
import com.example.myorder.model.dto.Pizza;
import com.example.myorder.model.entities.ProductCart;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class BottomSheetPizza extends BottomSheetDialogFragment {
    private PizzaBottomSheetBinding binding;
    private ICartActions cartActions;
    private RequestManager requestManager;

    Pizza pizza;
    int cost;
    String size;

    public BottomSheetPizza(RequestManager requestManager, ICartActions iCartActions) {
        this.requestManager = requestManager;
        this.cartActions = iCartActions;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PizzaBottomSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.chipGroup.setOnCheckedChangeListener((group, checkedId) -> setupCost());

        binding.buttonAdd.setOnClickListener(v -> {
            cartActions.increaseProduct(buildProductCart());
            Toast.makeText(getContext(), "Продукт добавлен", Toast.LENGTH_SHORT).show();
        });
        setupView();
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    public void setupView() {
        requestManager
                .load(pizza.logo)
                .into(binding.logo);

        binding.textViewName.setText(pizza.name);
        binding.textViewIngredients.setText(pizza.ingredients);

        setupCost();
    }

    private void setupCost() {
        int cost = pizza.cost25;
        String size = binding.chip25.getText().toString();
        if (binding.chip30.isChecked()) {
            cost = pizza.cost30;
            size = binding.chip30.getText().toString();
        } else if (binding.chip35.isChecked()) {
            cost = pizza.cost35;
            size = binding.chip35.getText().toString();
        }

        this.cost = cost;
        this.size = size;
        binding.textViewCost.setText(cost + "р.");
    }

    private ProductCart buildProductCart() {
        return new ProductCart.Builder()
                .withLogo(pizza.logo)
                .withName(pizza.name)
                .withIdProduct(pizza.idDocument)
                .withSize(size)
                .withCost_one(cost)
                .build();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}