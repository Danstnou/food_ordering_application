package com.example.myorder.view.stocks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.RequestManager;
import com.example.myorder.databinding.StockBottomSheetBinding;
import com.example.myorder.model.dto.Stock;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class BottomSheetStocks extends BottomSheetDialogFragment {
    private StockBottomSheetBinding binding;
    private RequestManager requestManager;

    Stock stock;

    public BottomSheetStocks(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = StockBottomSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public void setupView() {
        requestManager
                .load(stock.logo)
                .into(binding.logo);
        binding.textViewName.setText(stock.name);
        binding.textViewDescription.setText(stock.description);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}