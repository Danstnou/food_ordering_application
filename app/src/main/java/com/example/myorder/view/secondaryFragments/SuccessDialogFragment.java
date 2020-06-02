package com.example.myorder.view.secondaryFragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.example.myorder.R;
import com.example.myorder.model.repositories.CartRepository;

public class SuccessDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.label_success_order)
                .setPositiveButton(R.string.textButton_code, (dialog, id) -> {
                    CartRepository.getInstance(getActivity().getApplication()).deleteAll();
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                });
        return builder.create();
    }
}