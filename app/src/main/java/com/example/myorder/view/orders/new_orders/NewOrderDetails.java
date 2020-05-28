package com.example.myorder.view.orders.new_orders;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.myorder.R;
import com.example.myorder.ViewModel.moderator.ModeratorViewModel;
import com.example.myorder.ViewModel.orders.SharedNewOrderViewModel;
import com.example.myorder.databinding.NewOrderDetailsBinding;
import com.example.myorder.model.dto.Order;
import com.example.myorder.utils.Constants;
import com.example.myorder.view.orders.base.ProductAdapter;
import com.google.android.material.snackbar.Snackbar;

public class NewOrderDetails extends Fragment {
    private NewOrderDetailsBinding binding;
    private ModeratorViewModel viewModel;
    private SharedNewOrderViewModel sharedOrderViewModel;
    private Order order;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = NewOrderDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ModeratorViewModel.class);
        sharedOrderViewModel = new ViewModelProvider(getActivity()).get(SharedNewOrderViewModel.class);
        setHasOptionsMenu(true);
        observeOrder();
    }

    private void observeOrder() {
        sharedOrderViewModel.getOrder().observe(getViewLifecycleOwner(), order -> {
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.new_order_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.confirm_order) {
            viewModel.clickButtonConfirm(order);
            Snackbar.make(this.getView(), "Заказ подтверждён", Snackbar.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.delete_order) {

            new AlertDialog.Builder(getActivity()).setPositiveButton(R.string.textButton_code, (dialog, id) -> {
                viewModel.deleteNewOrder(order);
                Snackbar.make(this.getView(), "Заказ удалён", Snackbar.LENGTH_SHORT).show();

                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.popBackStack();
            })
                    .setNegativeButton(R.string.textButton_cancel, (dialog, id) -> {
                    })
                    .setTitle("Точно удалить заказ?")
                    .show();
        }
        return false;
    }
}