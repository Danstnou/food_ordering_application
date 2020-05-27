package com.example.myorder.view.orders.new_orders;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.myorder.R;
import com.example.myorder.ViewModel.moderator.ModeratorViewModel;
import com.example.myorder.ViewModel.orders.SharedNewOrderViewModel;
import com.example.myorder.model.dto.Order;
import com.example.myorder.utils.Constants;
import com.example.myorder.view.orders.base.ListOrders;

public class ListNewOrders extends ListOrders {
    private ModeratorViewModel viewModel;
    private SharedNewOrderViewModel sharedOrderViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(ModeratorViewModel.class);
        sharedOrderViewModel = new ViewModelProvider(getActivity()).get(SharedNewOrderViewModel.class);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void setupAdapter() {
        adapter = new OrdersAdapter() {
            @Override
            protected void clickListenerButtonDetails(Button button, Order order) {
                button.setOnClickListener(v -> {
                    sharedOrderViewModel.setOrder(order);
                    navController.navigate(R.id.action_nav_orders_to_newOrderDetailsFragment);
                });
            }

            @Override
            protected void setOptionalTextView(TextView textViewOptional, Order order) {
                textViewOptional.setText(Constants.dateFormat.format(order.date.toDate()));
            }
        };
    }

    @Override
    protected void observeOrderList() {
        viewModel.getNewOrders().observe(this.getViewLifecycleOwner(), orders ->
                this.adapter.setOrderList(orders));
    }
}