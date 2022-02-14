package com.silvia.rema;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.silvia.rema.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private int waktu_loading = 1000;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tinyDB = new TinyDB(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!tinyDB.getBoolean("keyLogin")){
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);

                }else{
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

        }, waktu_loading);


    }



}