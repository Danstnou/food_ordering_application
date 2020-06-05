package com.example.myorder.model.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myorder.model.dto.Order;
import com.example.myorder.model.entities.User;
import com.example.myorder.utils.ExecutorServiceInstance;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class OrderRepository {
    private FirebaseFirestore db;

    private CollectionReference collectionNewOrders;
    private CollectionReference collectionConfirmOrders;
    private CollectionReference collectionCompleteOrders;

    private String pathNewOrders = "new_orders";
    private String pathConfirmOrders = "confirm_orders";
    private String pathCompleteOrders = "complete_orders";

    private MutableLiveData<List<Order>> newOrderList;
    private MutableLiveData<List<Order>> confirmOrderList;
    private MutableLiveData<List<Order>> courierOrderList;

    private ListenerRegistration listenerNewOrders;
    private ListenerRegistration listenerConfirmOrders;
    private ListenerRegistration listenerOrdersCourier;

    private ExecutorService executorService;

    private OrderRepository() {
        db = FirebaseFirestore.getInstance();
        executorService = ExecutorServiceInstance.getInstance();

        collectionNewOrders = db.collection(pathNewOrders);
        collectionConfirmOrders = db.collection(pathConfirmOrders);
        collectionCompleteOrders = db.collection(pathCompleteOrders);
    }

    public static OrderRepository instance;

    public static OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    private List<Order> toOrders(QuerySnapshot queryDocumentSnapshots) {
        List<Order> orderList = null;

        if (!queryDocumentSnapshots.isEmpty()) {
            orderList = new ArrayList<>();

            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                try {
                    orderList.add(documentSnapshot.toObject(Order.class));
                } catch (Exception e) {
                }
            }
        }
        return orderList;
    }

    /*
     * Новые заказы
     */

    public void saveNewOrder(Order order) {
        executorService.execute(() -> collectionNewOrders.document(order.id).set(order));
    }

    public void observeNewOrders() {
        if (listenerNewOrders != null)
            return;

        executorService.execute(() -> listenerNewOrders = collectionNewOrders
                .addSnapshotListener((queryDocumentSnapshots, e) ->
                        this.newOrderList.postValue(toOrders(queryDocumentSnapshots))));
    }

    public MutableLiveData<List<Order>> getNewOrderList() {
        if (newOrderList == null) {
            newOrderList = new MutableLiveData<>();
            observeNewOrders();
        }
        return newOrderList;
    }

    // при удалении возвращать ответ, успешно ли. и так все вызовы, чтобы не возникло расхождений
    public void deleteNewOrder(Order order) {
        executorService.execute(() ->
                collectionNewOrders.document(order.id).delete().addOnSuccessListener(
                        aVoid -> {
                        }));
    }

    /*
     * Подтверждённые заказы
     */

    public void saveConfirmOrder(Order order) {
        executorService.execute(() -> collectionConfirmOrders.document(order.id).set(order));
    }

    public void updateCourier(Order order) {
        executorService.execute(() -> collectionConfirmOrders
                .document(order.id)
                .update("courier", order.courier)
                .addOnCompleteListener(task -> {
                })
        );
    }

    public void observeConfirmOrders() {
        if (listenerConfirmOrders != null)
            return;

        executorService.execute(() -> listenerConfirmOrders = collectionConfirmOrders
                .addSnapshotListener((queryDocumentSnapshots, e) ->
                        this.confirmOrderList.postValue(toOrders(queryDocumentSnapshots))));
    }

    public MutableLiveData<List<Order>> getConfirmOrderList() {
        if (confirmOrderList == null) {
            confirmOrderList = new MutableLiveData<>();
            observeConfirmOrders();
        }
        return confirmOrderList;
    }

    public void deleteConfirmOrder(Order order) {
        executorService.execute(() ->
                collectionConfirmOrders.document(order.id).delete().addOnSuccessListener(
                        aVoid -> {
                        }));
    }

    /*
     * Заказы курьера
     */

    public void observeOrdersCourier(User courier) {
        if (listenerOrdersCourier != null)
            return;

        executorService.execute(() -> listenerOrdersCourier = collectionConfirmOrders
                .whereEqualTo("id_courier", courier.getPhone())
                .addSnapshotListener((queryDocumentSnapshots, e) ->
                        this.courierOrderList.postValue(toOrders(queryDocumentSnapshots))));
    }

    public LiveData<List<Order>> getOrdersCourier(User courier) {
        if (courierOrderList == null) {
            courierOrderList = new MutableLiveData<>();
            observeOrdersCourier(courier);
        }
        return courierOrderList;
    }

    public void close() {
        if (listenerNewOrders != null)
            listenerNewOrders.remove();
        listenerNewOrders = null;
        newOrderList = null;

        if (listenerConfirmOrders != null)
            listenerConfirmOrders.remove();
        listenerConfirmOrders = null;
        confirmOrderList = null;

        if (listenerOrdersCourier != null)
            listenerOrdersCourier.remove();
        listenerOrdersCourier = null;
        courierOrderList = null;
    }
}