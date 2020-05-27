package com.example.myorder.view.orders.confirmOrders.courier;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myorder.R;
import com.example.myorder.ViewModel.courier.CourierViewModel;
import com.example.myorder.view.orders.confirmOrders.moderator.ConfirmOrderDetails;
import com.google.android.material.snackbar.Snackbar;

public class CourierOrderDetails extends ConfirmOrderDetails {
    CourierViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(CourierViewModel.class);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.courier_order_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.complete_order) {
            new AlertDialog.Builder(getActivity()).setPositiveButton(R.string.textButton_code, (dialog, id) -> {
                viewModel.closeOrder(order);
                Snackbar.make(this.getView(), "Заказ закрыт", Snackbar.LENGTH_SHORT).show();

                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.popBackStack();
            }).setNegativeButton(R.string.textButton_cancel, (dialog, id) -> {
            })
                    .setTitle("Точно закрыть заказ?")
                    .create()
                    .show();
        }
        return false;
    }
}
