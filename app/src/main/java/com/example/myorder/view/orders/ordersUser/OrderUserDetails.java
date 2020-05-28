package com.example.myorder.view.orders.ordersUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.myorder.ViewModel.profile.SharedUserViewModel;
import com.example.myorder.databinding.NewOrderDetailsBinding;
import com.example.myorder.model.dto.Order;
import com.example.myorder.utils.Constants;
import com.example.myorder.view.orders.base.ProductAdapter;

public class OrderUserDetails extends Fragment {
    private NewOrderDetailsBinding binding;
    private SharedUserViewModel sharedUserViewModel;
    private Order order;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = NewOrderDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedUserViewModel = new ViewModelProvider(getActivity()).get(SharedUserViewModel.class);
        setHasOptionsMenu(true);
        observeOrder();
    }

    private void observeOrder() {
        sharedUserViewModel.getOrder().observe(getViewLifecycleOwner(), order -> {
            this.order = order;
            setTextFromOrder();
            initRecyclerViewAndAdapter();
        });
    }

    private void setTextFromOrder() {
        binding.textViewDate.setText(Constants.dateFormat.format(order.date.toDate()));
        binding.textViewComment.setText('"' + order.comment + '"');
        binding.textViewCost.setText(order.total_cost + "р.");
        binding.chipPayment.setText(order.type_payment);
        binding.textViewName.setText(order.user.getName());
        binding.textViewPhone.setText(order.user.getPhone());
        binding.textViewAddress.setText(order.user.getAddress());
    }

    private void initRecyclerViewAndAdapter() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(new ProductAdapter(order.productList, Glide.with(this)));
        binding.recyclerView.addItemDecoration(Constants.itemDecoration);
    }
}