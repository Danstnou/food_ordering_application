package com.example.myorder.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.myorder.R;
import com.example.myorder.model.entities.User;
import com.example.myorder.model.repositories.OrderRepository;
import com.example.myorder.model.repositories.UserRepository;

public class MainActivityViewModel extends AndroidViewModel {
    UserRepository userRepository;
    OrderRepository orderRepository;

    LiveData<User> user;
    MutableLiveData<Boolean> isModerator = new MutableLiveData<>();
    MutableLiveData<Boolean> isCourier = new MutableLiveData<>();
    ;

    public MainActivityViewModel(Application application) {
        super(application);
        userRepository = UserRepository.getInstance();
        orderRepository = OrderRepository.getInstance();
    }

    /*
     * Пользователь
     */

    public LiveData<User> getUserOrGuest() {
        userRepository.checkAuthorization();
        transformationUser();
        return user;
    }

    public void transformationUser() {
        user = Transformations.map(userRepository.getAuthenticatedUser(), user -> {
            User currentUser = user;

            if (currentUser != null) {
                checkModerator();
                checkCourier();
            }

            if (currentUser == null) {
                currentUser = new User(getString(R.string.guest_name_header), "", "");
                isModerator.setValue(false);
                isCourier.setValue(false);
            } else if ("".equals(user.getName()))
                currentUser.setName(getString(R.string.empty_name_header));

            return currentUser;
        });
    }

    /*
     * Вошёл модератор
     */

    private void checkModerator() {
        User user = userRepository.getAuthenticatedUser().getValue();
        isModerator.setValue(user.getRoles() != null && user.getRoles().get("moderator"));
    }

    public MutableLiveData<Boolean> getIsModerator() {
        return isModerator;
    }

    /*
     * Вошёл курьер
     */

    public void checkCourier() {
        User user = userRepository.getAuthenticatedUser().getValue();
        isCourier.setValue(user.getRoles() != null && user.getRoles().get("courier"));
    }

    public MutableLiveData<Boolean> getIsCourier() {
        return isCourier;
    }

    /*
     * Строки из resource
     */

    private String getString(int id) {
        return getApplication().getString(id);
    }
}