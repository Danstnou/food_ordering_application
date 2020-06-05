package com.example.myorder.ViewModel.orders;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myorder.model.dto.Courier;
import com.example.myorder.model.dto.Order;
import com.example.myorder.model.repositories.OrderRepository;
import com.example.myorder.utils.ExecutorServiceInstance;

import java.util.concurrent.ExecutorService;

public class SharedConfirmOrderViewModel extends ViewModel {
    OrderRepository orderRepository;
    MutableLiveData<Order> order;
    ExecutorService executorService;

    public SharedConfirmOrderViewModel() {
        order = new MutableLiveData<>();
        orderRepository = OrderRepository.getInstance();
        executorService = ExecutorServiceInstance.getInstance();
    }

    public void setOrder(Order order) {
        executorService.execute(() -> this.order.postValue(order));
    }

    public MutableLiveData<Order> getOrder() {
        return order;
    }

    public void setCourier(Courier courier) {
        executorService.execute(() -> {
            Order order = getOrder().getValue();

            order.courier = courier;
            order.id_courier = courier.phone;

            orderRepository.updateCourier(order);
            this.order.postValue(order);
        });
    }
}