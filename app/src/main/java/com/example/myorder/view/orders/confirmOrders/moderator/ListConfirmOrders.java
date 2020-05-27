package com.example.myorder.view.orders.confirmOrders.moderator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.myorder.R;
import com.example.myorder.ViewModel.moderator.ModeratorViewModel;
import com.example.myorder.ViewModel.orders.SharedConfirmOrderViewModel;
import com.example.myorder.model.dto.Order;
import com.example.myorder.view.orders.base.ListOrders;

public class ListConfirmOrders extends ListOrders {
    ModeratorViewModel viewModel;
    SharedConfirmOrderViewModel sharedOrderViewModel;

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(ModeratorViewModel.class);
        sharedOrderViewModel = new ViewModelProvider(getActivity()).get(SharedConfirmOrderViewModel.class);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void setupAdapter() {
        adapter = new OrdersAdapter() {
            @Override
            protected void clickListenerButtonDetails(Button button, Order order) {
                button.setOnClickListener(v -> {
                    sharedOrderViewModel.setOrder(order);
                    navController.navigate(R.id.action_nav_orders_to_confirmOrderDetailsFragment);
                });
            }

            @Override
            protected void setOptionalTextView(TextView textViewOptional, Order order) {
                textViewOptional.setText(order.courier == null ? "Курьер не назначен" : order.courier.name);
            }
        };
    }

    @Override
    protected void observeOrderList() {
        this.viewModel.getConfirmOrders().observe(this.getViewLifecycleOwner(), orderList ->
                this.adapter.setOrderList(orderList));
    }
}