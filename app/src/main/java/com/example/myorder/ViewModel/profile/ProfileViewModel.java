package com.example.myorder.ViewModel.profile;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myorder.model.dto.Order;
import com.example.myorder.model.dto.UserDto;
import com.example.myorder.model.entities.User;
import com.example.myorder.model.repositories.OrderRepository;
import com.example.myorder.model.repositories.UserRepository;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {
    UserRepository userRepository;

    public ProfileViewModel(Application application) {
        super(application);
        userRepository = UserRepository.getInstance();
    }

    public List<AuthUI.IdpConfig> getProviders() {
        return userRepository.getProviders();
    }

    public int getRC_SIGN_IN() {
        return userRepository.getRC_SIGN_IN();
    }

    public void successfullySigned(IdpResponse response, FirebaseUser firebaseUser) {
        userRepository.successfullySigned(response, firebaseUser);
    }

    public LiveData<User> getUser() {
        userRepository.checkAuthorization();
        return userRepository.getAuthenticatedUser();
    }

    public AuthUI getAuthUI() {
        return userRepository.getAuthUI();
    }

    public void saveUser(UserDto user) {
        userRepository.saveUserByPhone(user);
    }

    public LiveData<List<Order>> getOrders() {
        return userRepository.getUserOrders();
    }

    public void logout() {
        userRepository.close();
        OrderRepository.getInstance().close();
    }
}