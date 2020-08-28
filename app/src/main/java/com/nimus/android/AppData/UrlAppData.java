package com.nimus.android.AppData;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UrlAppData {
    private String urlSlideJSON;
    private String urlPostJSON;
    private FirebaseStorage storage;
    private StorageReference reference;
    private  static UrlAppData instance;


    private UrlAppData(){
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference().child("https://console.firebase.google.com/project/nimus-commercia/storage/nimus-commercia.appspot.com/files/json/articles/featured");
    }

    public static UrlAppData getInstance(){
        if(instance == null){
            instance = new UrlAppData();
        }
        return instance;
    }

    public String SlideJson(){
        return urlSlideJSON;
    }

    public String PostJson(){
        return urlPostJSON;
    }

}
