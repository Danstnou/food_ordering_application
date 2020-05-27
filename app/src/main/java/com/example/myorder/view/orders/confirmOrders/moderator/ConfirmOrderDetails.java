package com.example.myorder.view.orders.confirmOrders.moderator;

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
import com.example.myorder.ViewModel.orders.SharedConfirmOrderViewModel;
import com.example.myorder.databinding.ConfirmOrderDetailsBinding;
import com.example.myorder.model.dto.Order;
import com.example.myorder.utils.Constants;
import com.example.myorder.view.orders.base.ProductAdapter;
import com.google.android.material.snackbar.Snackbar;

public class ConfirmOrderDetails extends Fragment {
    protected ConfirmOrderDetailsBinding binding;
    private ModeratorViewModel viewModel;
    private NavController navController;
    protected SharedConfirmOrderViewModel sharedOrderViewModel;
    protected Order order;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ConfirmOrderDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ModeratorViewModel.class);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        sharedOrderViewModel = new ViewModelProvider(getActivity()).get(SharedConfirmOrderViewModel.class);
        setHasOptionsMenu(true);
        observeOrder();
    }

    protected void observeOrder() {
        sharedOrderViewModel.getOrder().observe(getViewLifecycleOwner(), order -> {
            this.order = order;
            setTextFromOrder();
            initRecyclerViewAndAdapter();
        });
    }

    protected void setTextFromOrder() {
        binding.textViewDate.setText(Constants.dateFormat.format(order.date.toDate()));
        binding.textViewComment.setText('"' + order.comment + '"');
        binding.textViewCost.setText(order.total_cost + "р.");
        binding.chipPayment.setText(order.type_payment);
        binding.textViewName.setText(order.user.getName());
        binding.textViewPhone.setText(order.user.getPhone());
        binding.textViewAddress.setText(order.user.getAddress());
        binding.textViewCourier.setText(order.courier == null ? "Курьер не назначен" : order.courier.name);
    }

    protected void initRecyclerViewAndAdapter() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(new ProductAdapter(order.productCartList, Glide.with(this)));
        binding.recyclerView.addItemDecoration(Constants.itemDecoration);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.confirm_order_menu, menu);
    }

    private void showMessage(String message) {
        Snackbar.make(this.getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void back() {
        navController.popBackStack();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.appoint_courier) {
            navController.navigate(R.id.action_confirmOrderDetailsFragment_to_courierChoiceFragment);
        } else if (item.getItemId() == R.id.delete_order) {

            new AlertDialog.Builder(getActivity()).setPositiveButton(R.string.textButton_code, (dialog, id) -> {
                viewModel.deleteConfirmOrder(order);
                back("Заказ удалён");
            }).setNegativeButton(R.string.textButton_cancel, (dialog, id) -> {
            })
                    .setTitle("Точно удалить заказ?")
                    .create()
                    .show();

        } else if (item.getItemId() == R.id.close_order) {
            new AlertDialog.Builder(getActivity()).setPositiveButton(R.string.textButton_code, (dialog, id) -> {
                viewModel.closeOrder(order);
                back("Заказ закрыт");
            })
                    .setNegativeButton(R.string.textButton_cancel, (dialog, id) -> {
                    })
                    .setTitle("Точно закрыть заказ?")
                    .create()
                    .show();
        }
        return false;
    }

    private void back(String message){
        showMessage(message);
        back();
    }
}