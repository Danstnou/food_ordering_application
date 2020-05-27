package com.example.myorder.ViewModel.orderClient;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.myorder.ViewModel.LiveDataResult.ProductsAndTotalCost;
import com.example.myorder.model.dto.InputDataOrder;
import com.example.myorder.model.dto.Order;
import com.example.myorder.model.dto.UserDto;
import com.example.myorder.model.repositories.CartRepository;
import com.example.myorder.model.repositories.OrderRepository;
import com.example.myorder.model.repositories.UserRepository;
import com.example.myorder.utils.ExecutorServiceInstance;
import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.concurrent.ExecutorService;

public class OrderClientViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private CartRepository cartRepository;
    private OrderRepository orderRepository;

    LiveData userOrGuest;
    ExecutorService executorService;

    public OrderClientViewModel(Application application) {
        super(application);

        userRepository = UserRepository.getInstance();
        cartRepository = CartRepository.getInstance(application);
        orderRepository = OrderRepository.getInstance();

        userOrGuest = Transformations.map(userRepository.getAuthenticatedUser(), user ->
                user == null ?
                        new UserDto("", "", "") :
                        new UserDto(user.getName(), user.getPhone(), user.getAddress()));

        executorService = ExecutorServiceInstance.getInstance();
    }

    /*
     * Обработка заказа
     */

    public void toOrder(InputDataOrder inputDataOrder) {
        MediatorLiveData<ProductsAndTotalCost> combiningResults = new MediatorLiveData<>();
        addSource(combiningResults);
        isComplete(combiningResults, inputDataOrder);
    }

    public LiveData<UserDto> getUserOrGuest() {
        userRepository.checkAuthorization();
        return userOrGuest;
    }

    private void addSource(MediatorLiveData<ProductsAndTotalCost> combiningResults) {
        ProductsAndTotalCost userAndProducts = new ProductsAndTotalCost();

        combiningResults.addSource(cartRepository.getAll(), productCartList -> {
            userAndProducts.productCartList = productCartList;
            combiningResults.postValue(userAndProducts);
        });

        combiningResults.addSource(cartRepository.getTotalCostCart(), totalCost -> {
            userAndProducts.totalCost = totalCost;
            combiningResults.postValue(userAndProducts);
        });
    }

    private void isComplete(MediatorLiveData<ProductsAndTotalCost> combiningResults, InputDataOrder inputOrder) {
        combiningResults.observeForever(new Observer<ProductsAndTotalCost>() {
            @Override
            public void onChanged(ProductsAndTotalCost productsAndTotalCost) {
                if (!productsAndTotalCost.isComplete())
                    return;

                Order order = new Order(
                        new Timestamp(new Date()),
                        new UserDto(inputOrder.getName(), inputOrder.getPhone(), inputOrder.getAddress()),
                        productsAndTotalCost.productCartList,
                        productsAndTotalCost.totalCost,
                        inputOrder.getType_payment(),
                        inputOrder.getComment());

                orderRepository.saveNewOrder(order);
                combiningResults.removeObserver(this);
            }
        });
    }
}