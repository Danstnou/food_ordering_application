package com.example.myorder;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myorder.ViewModel.MainActivityViewModel;
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
        if (userOrGuest.getRoles() != null && userOrGuest.getRoles().get("moderator"))
            setVisibleModeratorMenu(true);
        else
            setVisibleModeratorMenu(false);
    }

    private void checkCourier(User userOrGuest) {
        if (userOrGuest.getRoles() != null && userOrGuest.getRoles().get("courier"))
            setVisibleCourierMenu(true);
        else
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}