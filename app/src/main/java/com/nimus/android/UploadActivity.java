package com.nimus.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText title,category,desc,url;
    private Button upload;
    private String titleText, categoryText, descText, urlText;
    private String name, email, uid, image;
    private DatabaseReference databaseReference;
    private Map<String,String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        title = findViewById(R.id.et_title);
        category = findViewById(R.id.et_category);
        desc = findViewById(R.id.et_description);
        url = findViewById(R.id.et_githubURL);
        upload = findViewById(R.id.button_upload_project);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Projects");

        title.setOnClickListener(this);
        category.setOnClickListener(this);
        desc.setOnClickListener(this);
        url.setOnClickListener(this);
        upload.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_upload_project:{
                getUserInput();
                getCurrentUserDetails();
                uploadData();

            }
        }
    }

    private void getUserInput(){
        titleText = title.getText().toString().trim();
        categoryText = category.getText().toString().trim();
        descText = desc.getText().toString().trim();
        urlText = url.getText().toString().trim();
    }

    private void uploadData(){
        if(titleText.length()<=0 || categoryText.length()<=0 || descText.length()<=0 || urlText.length()<=0){
            Toast.makeText(this, "Please fill all the fields!!", Toast.LENGTH_SHORT).show();
        }else{
            map.put("title",titleText);
            map.put("category",categoryText);
            map.put("desc",descText);
            map.put("url",urlText);
            map.put("user",name);
            map.put("email",email);
            map.put("uid",uid);
            map.put("image",image);

            databaseReference.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(UploadActivity.this, "uploaded", Toast.LENGTH_SHORT).show();
                        title.setText(null);
                        category.setText(null);
                        desc.setText(null);
                        url.setText(null);
                    }
                    else{
                        Toast.makeText(UploadActivity.this, "Failed to upload!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    void getCurrentUserDetails(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            name = user.getDisplayName();
            email = user.getEmail();
            uid = user.getUid();
            image = user.getPhotoUrl().toString();
        }
    }
}