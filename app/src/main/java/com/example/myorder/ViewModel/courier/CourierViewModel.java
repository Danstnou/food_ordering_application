package com.example.myorder.ViewModel.courier;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myorder.model.dto.Order;
import com.example.myorder.model.repositories.OrderRepository;
import com.example.myorder.model.repositories.UserRepository;

import java.util.List;

public class CourierViewModel extends ViewModel {
    UserRepository userRepository;
    OrderRepository orderRepository;


    public CourierViewModel() {
        userRepository = UserRepository.getInstance();
        orderRepository = OrderRepository.getInstance();
    }

    public LiveData<List<Order>> getOrders() {
        return orderRepository.getOrdersCourier(
                userRepository.getAuthenticatedUser().getValue());
    }

    public void closeOrder(Order order) {
        userRepository.saveOrderUser(order);
        orderRepository.deleteConfirmOrder(order);
    }
}