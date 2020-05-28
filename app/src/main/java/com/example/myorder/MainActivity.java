package com.example.myorder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myorder.ViewModel.MainActivityViewModel;
import com.example.myorder.ViewModel.courier.CourierViewModel;
import com.example.myorder.ViewModel.moderator.ModeratorViewModel;
import com.example.myorder.model.entities.User;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private MainActivityViewModel mainActivityViewModel;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        navigationView = findViewById(R.id.nav_view);

        createNavigation();
        setVisibleModeratorMenu(false);
        setVisibleCourierMenu(false);
        observeUser();
    }

    private void createNavigation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_products, R.id.nav_profile,
                R.id.nav_cart,
                R.id.nav_orders_moderator,
                R.id.nav_orders_courier,
                R.id.about)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void observeUser() {
        mainActivityViewModel.getUserOrGuest().observe(this, userOrGuest -> {
            setTextUser(userOrGuest);
            checkModerator(userOrGuest);
            checkCourier(userOrGuest);
        });
    }

    private void setTextUser(User userOrGuest) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header_nv = navigationView.getHeaderView(0);

        TextView textViewName = header_nv.findViewById(R.id.textView_header_name);
        TextView textViewPhone = header_nv.findViewById(R.id.textView_header_phone);

        textViewName.setText(userOrGuest.getName());
        textViewPhone.setText(userOrGuest.getPhone());
    }

    private void checkModerator(User userOrGuest) {
        if (userOrGuest.getRoles() != null && userOrGuest.getRoles().get("moderator")) {
            setVisibleModeratorMenu(true);
            observeOrdersModerator();
        } else
            setVisibleModeratorMenu(false);
    }

    private void checkCourier(User userOrGuest) {
        if (userOrGuest.getRoles() != null && userOrGuest.getRoles().get("courier")) {
            setVisibleCourierMenu(true);
            observeOrdersCouriers();
        } else
            setVisibleCourierMenu(false);
    }

    private void setVisibleModeratorMenu(Boolean visibility) {
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_orders_moderator).setVisible(visibility);
    }

    private void setVisibleCourierMenu(Boolean visibility) {
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_orders_courier).setVisible(visibility);
    }

    /*
     * Если модератор, то следим за всеми новыми заказами
     */

    protected void observeOrdersModerator() {
        ModeratorViewModel viewModel = new ViewModelProvider(this).get(ModeratorViewModel.class);
        viewModel.getNewOrders().observe(this, orders -> {
            int size = orders.size();
            if (size != 0)
                createNotificationChannel(size);
        });
    }

    /*
     * Если курьер, то следим за его новыми заказами
     */

    protected void observeOrdersCouriers() {
        CourierViewModel viewModel = new ViewModelProvider(this).get(CourierViewModel.class);
        viewModel.getOrders().observe(this, orders -> {
            int size = orders.size();
            if (size != 0)
                createNotificationChannel(size);
        });
    }

    private void createNotificationChannel(int size) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this, "new orders")
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
            notificationManager.notify(0, builder.build());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}