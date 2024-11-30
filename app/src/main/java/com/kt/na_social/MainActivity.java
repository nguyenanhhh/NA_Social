package com.kt.na_social;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.kt.na_social.model.User;
import com.kt.na_social.viewmodel.AuthStore;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static NavController NAV_CONTROLLER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.onInit();
    }

    public void onInit() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_nav_host);
        if (navHostFragment == null) {
            return;
        }
        NAV_CONTROLLER = navHostFragment.getNavController();
    }

    @Override
    public boolean onNavigateUp() {
        NAV_CONTROLLER = Navigation.findNavController(this, R.id.main_nav_host);
        return NAV_CONTROLLER.navigateUp() || super.onSupportNavigateUp();
    }
}