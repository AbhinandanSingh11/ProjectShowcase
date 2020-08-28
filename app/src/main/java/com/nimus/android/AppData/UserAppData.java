package com.nimus.android.AppData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAppData {
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private static UserAppData instance;


    private UserAppData(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    public static UserAppData getInstance(){
        if(instance == null){
            instance = new UserAppData();
        }

        return instance;
    }

    public FirebaseUser getCurrentUser(){
        return user;
    }
}
