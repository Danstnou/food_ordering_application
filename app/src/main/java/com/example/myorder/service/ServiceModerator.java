package com.example.myorder.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.myorder.R;
import com.example.myorder.model.dto.Order;
import com.example.myorder.model.repositories.OrderRepository;

import java.util.List;

public class ServiceModerator extends Service {
    protected OrderRepository orderRepository;
    protected LiveData newOrders;
    protected Observer observer;

    protected int count = 0;

    public ServiceModerator() {
        observer = (Observer<List<Order>>) orders -> {
            if (orders == null) {
                count = 0;
                return;
            }

            int newCount = orders.size();
            if (newCount >= count) {
                createNotificationChannel(orders.size());
                count = newCount;
            }
            count = newCount;
        };
        orderRepository = OrderRepository.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        observeNewOrders();
    }

    protected void observeNewOrders() {
        newOrders = orderRepository.getNewOrderList();
        newOrders.observeForever(observer);
    }

    protected void createNotificationChannel(int size) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(getApplicationContext(), "new orders")
                            .setSmallIcon(R.drawable.ic_keyboard_arrow_right_black_24dp)
                            .setContentTitle("Новый заказ")
                            .setContentText(size + " новых заказов!")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationChannel channel = new NotificationChannel(
                    "new orders",
                    "Новый заказ",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(size + " новых заказов!");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(1, builder.build());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        count = 0;
        newOrders.removeObserver(observer);
        observer = null;
        super.onDestroy();
    }
}