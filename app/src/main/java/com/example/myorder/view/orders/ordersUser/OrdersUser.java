package com.example.myorder.view.orders.ordersUser;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.myorder.R;
import com.example.myorder.ViewModel.profile.ProfileViewModel;
import com.example.myorder.ViewModel.profile.SharedUserViewModel;
import com.example.myorder.model.dto.Order;
import com.example.myorder.utils.Constants;
import com.example.myorder.view.orders.base.ListOrders;

public class OrdersUser extends ListOrders {
    ProfileViewModel viewModel;
    SharedUserViewModel sharedUserViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);
        sharedUserViewModel = new ViewModelProvider(getActivity()).get(SharedUserViewModel.class);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void observeOrderList() {
        viewModel.getOrders().observe(getViewLifecycleOwner(), orders ->
                this.adapter.setOrderList(orders));
    }

    @Override
    protected void setupAdapter() {
        adapter = new OrdersAdapter() {
            @Override
            protected void clickListenerButtonDetails(Button button, Order order) {
                button.setOnClickListener(v -> {
                    sharedUserViewModel.setOrder(order);
                    navController.navigate(R.id.action_ordersUser_to_orderUserDetails);
                });
            }

            @Override
            protected void setOptionalTextView(TextView textViewOptional, Order order) {
                textViewOptional.setText(Constants.dateFormat.format(order.date.toDate()));
            }
        };
    }
}
