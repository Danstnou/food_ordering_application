package com.example.myorder.model.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.myorder.model.dto.Order;
import com.example.myorder.model.dto.UserDto;
import com.example.myorder.model.entities.User;
import com.example.myorder.utils.ExecutorServiceInstance;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class UserRepository {
    // идентификация
    private FirebaseAuth auth;
    private AuthUI authUI;
    private List<AuthUI.IdpConfig> providers;
    private int RC_SIGN_IN = 1;

    // БД
    private FirebaseFirestore db;
    private CollectionReference collection;
    private String collectionPath = "users";
    private List<String> listRoles = Arrays.asList("user", "moderator", "admin", "courier");

    private MutableLiveData<User> authenticatedUser;
    private ListenerRegistration subscribeUser;

    private ExecutorService executorService;

    private UserRepository() {
        this.auth = FirebaseAuth.getInstance();
        this.authUI = AuthUI.getInstance();
        providers = Arrays.asList(new AuthUI.IdpConfig
                .PhoneBuilder()
                .setDefaultCountryIso("ru")
                .build());

        db = FirebaseFirestore.getInstance();
        collection = db.collection(collectionPath);

        authenticatedUser = new MutableLiveData<>();
        executorService = ExecutorServiceInstance.getInstance();
    }

    public static UserRepository instance;

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    /*
     * Вход
     */

    public void checkAuthorization() {
        executorService.execute(() -> {
            FirebaseUser firebaseUser = getFirebaseUser();
            if (firebaseUser == null)
                authenticatedUser.postValue(null);
            else if (authenticatedUser.getValue() == null)
                setAuthUser(firebaseUser);
        });
    }

    public void checkIsNewUser(IdpResponse response, FirebaseUser firebaseUser) {
        if (response.isNewUser())
            saveUserByPhone(new UserDto("", firebaseUser.getPhoneNumber(), ""));
    }

    public void successfullySigned(IdpResponse response, FirebaseUser firebaseUser) {
        checkIsNewUser(response, firebaseUser);
        setAuthUser(firebaseUser);
    }

    /*
     * Формируем пользователя с правами
     */

    private void setAuthUser(FirebaseUser firebaseUser) {
        executorService.execute(() -> {
            String phone = firebaseUser.getPhoneNumber();

            Task taskFirestore = collection.document(phone).get();
            Task taskToken = getFirebaseUser().getIdToken(true);
            Task<List<Object>> allTasks = Tasks.whenAllSuccess(taskFirestore, taskToken);

            allTasks.addOnSuccessListener(objects -> {
                UserDto userDto = getUserDto((DocumentSnapshot) objects.get(0), phone);
                Map<String, Boolean> roles = getUserRoles(((GetTokenResult) objects.get(1)).getClaims());

                this.authenticatedUser.postValue(new User(userDto, roles));

                if (subscribeUser == null)
                    observeUser(phone);
            });
        });
    }

    private UserDto getUserDto(DocumentSnapshot documentSnapshot, String phone) {
        UserDto userDTO;
        if (documentSnapshot.exists())
            userDTO = documentSnapshot.toObject(UserDto.class);
        else {
            userDTO = new UserDto("", phone, "");
            saveUserByPhone(userDTO);
        }
        return userDTO;
    }

    private Map<String, Boolean> getUserRoles(Map<String, Object> claims) {
        Map<String, Boolean> roles = new HashMap<>();

        for (String role : listRoles)
            roles.put(role, claims.get(role) != null);

        return roles;
    }

    private void observeUser(String phone) {
        executorService.execute(() -> {
            subscribeUser = collection.document(phone).addSnapshotListener((documentSnapshot, e) -> {
                if (documentSnapshot.exists()) {
                    User user = this.authenticatedUser.getValue();
                    user.setFieldsDto(documentSnapshot.toObject(UserDto.class));
                    this.authenticatedUser.postValue(user);
                }
            });
        });
    }

    /*
     * Сохранение пользователя
     */

    public void saveUserByPhone(UserDto user) {
        executorService.execute(() -> collection.document(user.getPhone()).set(user));
    }

    /*
     * Добавление заказов пользователю
     */

    public void saveOrderUser(Order order) {
        executorService.execute(() -> collection.document(order.user.getPhone())
                .collection("orders")
                .document(order.date.toString())
                .set(order));
    }

    /*
     * История заказов пользователя
     */

    public MutableLiveData<List<Order>> getUserOrders() {
        MutableLiveData<List<Order>> mutableLiveData = new MutableLiveData<>();
        executorService.execute(() -> collection
                .document(getFirebaseUser().getPhoneNumber())
                .collection("orders")
                .get()
                .addOnSuccessListener(task -> {
                    try {
                        List<Order> orders = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot: task.getDocuments())
                            orders.add(documentSnapshot.toObject(Order.class));

                        mutableLiveData.postValue(orders);
                    } catch (Exception e) {
                    }
                }));
        return mutableLiveData;
    }


    /*
     * Геттеры
     */

    public MutableLiveData<User> getAuthenticatedUser() {
        return authenticatedUser;
    }

    public FirebaseUser getFirebaseUser() {
        return auth.getCurrentUser();
    }

    public List<AuthUI.IdpConfig> getProviders() {
        return providers;
    }

    public int getRC_SIGN_IN() {
        return RC_SIGN_IN;
    }

    public AuthUI getAuthUI() {
        return authUI;
    }

    /*
     * Выход
     */

    public void close() {
        executorService.execute(() -> {
            if (subscribeUser != null)
                subscribeUser.remove();
            subscribeUser = null;
            authenticatedUser.postValue(null);
        });
    }
}