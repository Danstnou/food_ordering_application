package com.example.myorder.view.secondaryFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myorder.R;
import com.example.myorder.ViewModel.orderClient.OrderClientViewModel;
import com.example.myorder.databinding.ClientOrderFragmentBinding;
import com.example.myorder.model.dto.InputDataOrder;

public class OrderClientFragment extends Fragment {
    private ClientOrderFragmentBinding binding;
    private OrderClientViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ClientOrderFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(OrderClientViewModel.class);

        observeUser();
        clickListenerOrder();
    }

    private void setEditTextUser(String name, String phone, String address) {
        binding.textFieldName.getEditText().setText(name);
        binding.textFieldPhone.getEditText().setText(phone);
        binding.textFieldAddress.getEditText().setText(address);
    }

    private void observeUser() {
        viewModel.getUserOrGuest().observe(getViewLifecycleOwner(), userOrGuest ->
                setEditTextUser(userOrGuest.getName(), userOrGuest.getPhone(), userOrGuest.getAddress()));
    }

    private void clickListenerOrder() {
        binding.buttonOrder.setOnClickListener(v -> {
            String name = binding.textFieldName.getEditText().getText().toString();
            String phone = binding.textFieldPhone.getEditText().getText().toString();
            String address = binding.textFieldAddress.getEditText().getText().toString();
            String type_payment = binding.chipPayment.getCheckedChipId() == binding.chipCard.getId() ?
                    getString(R.string.card) :
                    getString(R.string.cash);
            String comment = binding.textFieldComment.getEditText().getText().toString();

            viewModel.toOrder(new InputDataOrder(name, phone, address, type_payment, comment));
            dialogSuccessOrder();
        });
    }

    private void dialogSuccessOrder() {
        new SuccessDialogFragment().show(getActivity().getSupportFragmentManager(), "");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
