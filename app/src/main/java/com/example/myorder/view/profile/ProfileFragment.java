package com.example.myorder.view.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myorder.R;
import com.example.myorder.ViewModel.profile.ProfileViewModel;
import com.example.myorder.databinding.FragmentProfileBinding;
import com.example.myorder.model.dto.UserDto;
import com.example.myorder.model.entities.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import static android.app.Activity.RESULT_OK;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);

        setVisibilityButtonSignIn(INVISIBLE);
        setVisibilityElementsUser(INVISIBLE);
        setVisibilityProgressBar(VISIBLE); // СРАЗУ ПОЯВЛЯЕТСЯ КНОПКА

        observeUser();
    }

    private void listenerButtonSignIn() {
        binding.buttonSignIn.setOnClickListener(v -> {
            binding.buttonSignIn.setVisibility(INVISIBLE);

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(profileViewModel.getProviders())
                            .build(),
                    profileViewModel.getRC_SIGN_IN());

        });
    }

    private void observeUser() {
        profileViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                setVisibilityProgressBar(INVISIBLE);
                setVisibilityElementsUser(INVISIBLE);
                setVisibilityButtonSignIn(VISIBLE);

                listenerButtonSignIn();
            } else {
                setVisibilityProgressBar(INVISIBLE);
                setVisibilityButtonSignIn(INVISIBLE);
                setVisibilityElementsUser(VISIBLE);

                setTextFieldsUser(user);

                listenerButtonSave();
                listenerButtonHistory();
                listenerButtonLogout();
            }
        });
    }

    private void setTextFieldsUser(User user) {
        binding.textFieldName.getEditText().setText(user.getName());
        binding.textFieldPhone.getEditText().setText(user.getPhone());
        binding.textFieldAddress.getEditText().setText(user.getAddress());
    }

    private void setVisibilityElementsUser(int status) {
        binding.cardView.setVisibility(status);
        binding.buttonLogout.setVisibility(status);
        binding.buttonHistory.setVisibility(status);
    }

    private void setVisibilityButtonSignIn(int status) {
        binding.buttonSignIn.setVisibility(status);
    }

    private void setVisibilityProgressBar(int status) {
        binding.progressBar.setVisibility(status);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == profileViewModel.getRC_SIGN_IN()) {

            if (resultCode == RESULT_OK) {
                profileViewModel.successfullySigned(
                        IdpResponse.fromResultIntent(data),
                        FirebaseAuth.getInstance().getCurrentUser());

                setVisibilityProgressBar(VISIBLE);
            } else
                setVisibilityButtonSignIn(VISIBLE);
        }
    }

    private void listenerButtonLogout() {
        binding.buttonLogout.setOnClickListener(v -> {
            profileViewModel.logout();

            profileViewModel.getAuthUI()
                    .signOut(getContext())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            setVisibilityElementsUser(INVISIBLE);
                        }
                    });
        });
    }

    private void listenerButtonSave() {
        binding.buttonSave.setOnClickListener(v -> {
            String name = binding.textFieldName.getEditText().getText().toString();
            String phone = binding.textFieldPhone.getEditText().getText().toString();
            String address = binding.textFieldAddress.getEditText().getText().toString();

            profileViewModel.saveUser(new UserDto(name, phone, address));
            showMessage(getString(R.string.profile_refresh));
        });
    }

    private void listenerButtonHistory() {
        binding.buttonHistory.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.action_nav_profile_to_ordersUser);
        });
    }

    private void showMessage(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}