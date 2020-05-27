package com.example.myorder.view.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myorder.ViewModel.about.AboutViewModel;
import com.example.myorder.databinding.AboutBinding;

public class AboutFragment extends Fragment {
    AboutBinding binding;
    AboutViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AboutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AboutViewModel.class);
        listenerAbout();
    }

    private void listenerAbout() {
        viewModel.getAbout().observe(getViewLifecycleOwner(), about -> {

            StringBuilder address = new StringBuilder();
            for (String a : about.address) {
                address.append(a + "\n");
            }
            binding.textViewAddress.setText(address.toString());

            StringBuilder contacts = new StringBuilder();
            for (String a : about.contacts) {
                contacts.append(a + "\n");
            }
            binding.textViewContacts.setText(contacts.toString());

            StringBuilder mode = new StringBuilder();
            for (String a : about.mode) {
                mode.append(a + "\n");
            }
            binding.textViewMode.setText(mode.toString());

            binding.textViewAbout.setText(about.about);
        });
    }
}