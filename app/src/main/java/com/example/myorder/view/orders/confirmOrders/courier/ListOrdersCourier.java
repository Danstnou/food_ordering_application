package com.example.myorder.view.orders.confirmOrders.courier;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.myorder.R;
import com.example.myorder.ViewModel.courier.CourierViewModel;
import com.example.myorder.ViewModel.orders.SharedConfirmOrderViewModel;
import com.example.myorder.model.dto.Order;
import com.example.myorder.view.orders.base.ListOrders;

public class ListOrdersCourier extends ListOrders {
    CourierViewModel viewModel;
    SharedConfirmOrderViewModel sharedOrderViewModel;

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(CourierViewModel.class);
        sharedOrderViewModel = new ViewModelProvider(getActivity()).get(SharedConfirmOrderViewModel.class);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void observeOrderList() {
        this.viewModel.getOrders().observe(this.getViewLifecycleOwner(), orderList ->
                this.adapter.setOrderList(orderList));
    }

    @Override
    protected void setupAdapter() {
        adapter = new OrdersAdapter() {
            @Override
            protected void clickListenerButtonDetails(Button button, Order order) {
                button.setOnClickListener(v -> {
                    sharedOrderViewModel.setOrder(order);
                    navController.navigate(R.id.action_listOrdersCourier_to_courierOrderDetails);
                });
            }

            @Override
            protected void setOptionalTextView(TextView textViewOptional, Order order) {
                textViewOptional.setText(order.courier.name);
            }
        };
    }
}
