package com.nimus.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setStatusBarColor(getResources().getColor(R.color.white,getTheme()));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    finish();
                    finishAffinity();
                }else{
                    startActivity(new Intent(SplashScreen.this,LoginActivity.class));
                    finish();
                    finishAffinity();
                }
            }
        },5000);
    }
}