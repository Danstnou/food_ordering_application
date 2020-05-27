package com.example.myorder.view.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myorder.databinding.FragmentTabsBinding;
import com.example.myorder.view.products.dessert.DessertFragment;
import com.example.myorder.view.products.pizza.PizzaFragment;
import com.example.myorder.view.products.rolls.RollsFragment;
import com.example.myorder.view.stocks.StocksFragment;
import com.google.android.material.tabs.TabLayoutMediator;

public class ProductsFragment extends Fragment {
    FragmentTabsBinding binding;
    ProductsFragmentAdapter ordersFragmentAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTabsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private String getNameTab(int position) {
        switch (position) {
            case 0:
                return "Акции";
            case 1:
                return "Пицца";
            case 2:
                return "Роллы";
            default:
                return "Десерты";
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ordersFragmentAdapter = new ProductsFragmentAdapter(this);
        binding.pager.setAdapter(ordersFragmentAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.pager,
                (tab, position) -> tab.setText(getNameTab(position))
        ).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    static class ProductsFragmentAdapter extends FragmentStateAdapter {
        private StocksFragment stocksFragment;
        private PizzaFragment pizzaFragment;
        private RollsFragment rollsFragment;
        private DessertFragment dessertFragment;

        public ProductsFragmentAdapter(@NonNull Fragment fragment) {
            super(fragment);

            stocksFragment = new StocksFragment();
            pizzaFragment = new PizzaFragment();
            rollsFragment = new RollsFragment();
            dessertFragment = new DessertFragment();
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return stocksFragment;
                case 1:
                    return pizzaFragment;
                case 2:
                    return rollsFragment;
                default:
                    return dessertFragment;
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}