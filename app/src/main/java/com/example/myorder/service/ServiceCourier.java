package com.example.myorder.service;

import com.example.myorder.model.entities.User;
import com.example.myorder.model.repositories.UserRepository;

public class ServiceCourier extends ServiceModerator {
    UserRepository userRepository;

    public ServiceCourier() {
        super();
        userRepository = UserRepository.getInstance();
    }

    @Override
    protected void observeNewOrders() {
        User user = userRepository.getAuthenticatedUser().getValue();
        newOrders = orderRepository.getOrdersCourier(user);
        newOrders.observeForever(observer);
    }
}