package com.example.myorder.ViewModel.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myorder.model.dto.Order;
import com.example.myorder.utils.ExecutorServiceInstance;

import java.util.concurrent.ExecutorService;

public class SharedUserViewModel extends ViewModel {
    MutableLiveData<Order> order;
    ExecutorService executorService;

    public SharedUserViewModel() {
        order = new MutableLiveData<>();
        executorService = ExecutorServiceInstance.getInstance();
    }

    public void setOrder(Order order) {
        executorService.execute(() -> this.order.postValue(order));
    }

    public MutableLiveData<Order> getOrder() {
        return order;
    }
}
