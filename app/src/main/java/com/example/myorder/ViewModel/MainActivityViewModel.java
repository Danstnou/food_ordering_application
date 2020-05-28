package com.example.myorder.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.myorder.R;
import com.example.myorder.ViewModel.moderator.ModeratorViewModel;
import com.example.myorder.model.entities.User;
import com.example.myorder.model.repositories.UserRepository;

public class MainActivityViewModel extends AndroidViewModel {
    ModeratorViewModel moderatorViewModel;
    UserRepository userRepository;
    LiveData<User> user;

    public MainActivityViewModel(Application application) {
        super(application);
        userRepository = UserRepository.getInstance();
        user = Transformations.map(userRepository.getAuthenticatedUser(), user -> {
                    User currentUser = user;
                    if (currentUser == null)
                        currentUser = new User(getString(R.string.guest_name_header), "", "");
                    else if ("".equals(user.getName()))
                        currentUser.setName(getString(R.string.empty_name_header));

                    return currentUser;
                }
        );
    }

    private String getString(int id) {
        return getApplication().getString(id);
    }

    public LiveData<User> getUserOrGuest() {
        userRepository.checkAuthorization();
        return user;
    }
}