package com.example.myorder.ViewModel.moderator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myorder.model.dto.Courier;
import com.example.myorder.model.dto.Order;
import com.example.myorder.model.repositories.CouriersRepository;
import com.example.myorder.model.repositories.OrderRepository;
import com.example.myorder.model.repositories.UserRepository;

import java.util.List;

public class ModeratorViewModel extends ViewModel {
    OrderRepository orderRepository;
    CouriersRepository couriersRepository;
    UserRepository userRepository;

    public ModeratorViewModel() {
        orderRepository = OrderRepository.getInstance();
        couriersRepository = CouriersRepository.getInstance();
        userRepository = UserRepository.getInstance();
    }

    /*
     * Получение новых и подтверждённых заказов
     */

    public LiveData<List<Order>> getNewOrders() {
        return orderRepository.getNewOrderList();
    }

    public LiveData<List<Order>> getConfirmOrders() {
        return orderRepository.getConfirmOrderList();
    }

    /*
     * Нажатие на кнопку подтверждения заказа
     */

    public void clickButtonConfirm(Order order) {
        saveConfirmOrder(order);
        deleteNewOrder(order);
    }

    public void saveConfirmOrder(Order order) {
        orderRepository.saveConfirmOrder(order);
    }

    public void deleteNewOrder(Order order) {
        orderRepository.deleteNewOrder(order);
    }

    /*
     * Получаем список курьеров
     */

    public LiveData<List<Courier>> getCouriersList() {
        return couriersRepository.getCouriersList();
    }

    /*
     * Удаляем заказ
     */

    public void deleteConfirmOrder(Order order) {
        orderRepository.deleteConfirmOrder(order);
    }

    public void closeOrder(Order order) {
        userRepository.saveOrderUser(order);
        deleteConfirmOrder(order);
    }
}