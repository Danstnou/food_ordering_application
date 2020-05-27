package com.example.myorder.view.orders.swipeViews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myorder.databinding.FragmentTabsBinding;
import com.example.myorder.view.orders.base.ListOrders;
import com.example.myorder.view.orders.confirmOrders.moderator.ListConfirmOrders;
import com.example.myorder.view.orders.new_orders.ListNewOrders;
import com.google.android.material.tabs.TabLayoutMediator;

public class OrdersCollectionFragment extends Fragment {
    FragmentTabsBinding binding;

    ListOrders newOrdersFragment;
    ListOrders confirmOrdersFragment;

    public OrdersCollectionFragment() {
        newOrdersFragment = new ListNewOrders();
        confirmOrdersFragment = new ListConfirmOrders();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTabsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private String getNameTab(int position) {
        return position == 0 ? "Новые" : "Подтверждённые";
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.pager.setAdapter(new OrdersCollectionAdapter(this));

        new TabLayoutMediator(binding.tabLayout, binding.pager, (tab, position) ->
                tab.setText(getNameTab(position))).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class OrdersCollectionAdapter extends FragmentStateAdapter {

        public OrdersCollectionAdapter(Fragment fragment) {
            super(fragment);
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return position == 0 ? newOrdersFragment : confirmOrdersFragment;
        }
    }
}