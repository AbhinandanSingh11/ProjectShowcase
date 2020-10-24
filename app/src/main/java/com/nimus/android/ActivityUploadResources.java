package com.nimus.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nimus.android.AppData.EventsAppData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivityUploadResources extends AppCompatActivity implements View.OnClickListener {

    private RadioButton radioButtonVideo,radioButtonOthers;
    private EditText title,desc,url;
    private Button upload;
    private ImageView back;
    private String titleText, categoryText, descText, urlText;
    private String name, email, uid, image;
    private String date;
    private int resourceType;
    private DatabaseReference databaseReferenceResources;
    private Map<String,Object> map = new HashMap<>();
    private CardView progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_resources);

        radioButtonVideo = findViewById(R.id.radioVideo);
        radioButtonOthers = findViewById(R.id.radioOthers);
        title = findViewById(R.id.et_titleResource);
        desc = findViewById(R.id.et_descriptionResource);
        url = findViewById(R.id.et_githubURLResource);
        upload = findViewById(R.id.button_upload_resource);
        back = findViewById(R.id.backResource);
        progressBar = findViewById(R.id.progressUploadResources);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Log.d("UploadResourcesLogs", "Upload Clicked");
                getUserInput();
                getCurrentUserDetails();
                getEventDetails();
                getFormattedDate();
                uploadData();
            }
        });

        radioButtonVideo.setOnClickListener(this);
        radioButtonOthers.setOnClickListener(this);
        title.setOnClickListener(this);
        desc.setOnClickListener(this);
        url.setOnClickListener(this);
        back.setOnClickListener(this);

        databaseReferenceResources = FirebaseDatabase.getInstance().getReference("eventResources");


    }


    @Override
    public void onClick(View v) {



        switch (v.getId()){
            case R.id.radioVideo:
                if(radioButtonVideo.isChecked())
                resourceType = 0;
                break;
            case R.id.radioOthers:
                if(radioButtonOthers.isChecked())
                resourceType = 1;
                break;

            case R.id.backResource:{
                super.onBackPressed();
                break;
            }
        }
    }

    private void getUserInput(){
        Log.d("UploadResourcesLogs", "getUserInput Called");
        titleText = title.getText().toString().trim();
        descText = desc.getText().toString().trim();
        urlText = url.getText().toString().trim();
        Log.d("UploadResourcesLogs", "getUserInput Executed");
    }

    private void uploadData(){
        Log.d("UploadResourcesLogs", "uploadData Called");
        if(titleText.length()<=0 || descText.length()<=0 || urlText.length()<=0){
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
            map.put("cost","free");
            if(resourceType == 0){
                map.put("resType","video");
                map.put("type",0);
            }else{
                map.put("resType","code");
                map.put("type",1);
            }
            map.put("date",date);

            if(map.containsKey("imageURL")){



                databaseReferenceResources.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ActivityUploadResources.this, "Thanks For Contributing to DSC CU", Toast.LENGTH_SHORT).show();
                            title.setText(null);
                            desc.setText(null);
                            url.setText(null);
                            progressBar.setVisibility(View.GONE);
                        }
                        else{
                            Toast.makeText(ActivityUploadResources.this, "Failed to upload!!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }else{
                Log.d("Image Not found!!", "Ac0");
            }


        }
    }

    void getCurrentUserDetails(){
        Log.d("UploadResourcesLogs", "getCurrentUserDetails Called");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            name = user.getDisplayName();
            email = user.getEmail();
            uid = user.getUid();
            image = user.getPhotoUrl().toString();
        }
        Log.d("UploadResourcesLogs", "getCurrentUserDetails Executed");
    }

    void getFormattedDate(){
        Date mDate= Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
        date = dateFormat.format(mDate);
    }

    void getEventDetails(){
        Log.d("UploadResourcesLogs", "getEventDetails Called");
        DatabaseReference databaseReferenceEvents = FirebaseDatabase.getInstance().getReference("specialEvents");
        databaseReferenceEvents.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    com.nimus.android.Models.SpecialEvents model = snapshot.getValue(com.nimus.android.Models.SpecialEvents.class);
                    if(model != null){
                        if(model.getEventID().equals(EventsAppData.getInstance().getSpecialEvents().get(0).getEventID()))
                            categoryText = model.getDomain();
                            map.put("imageURL",model.getImage());
                            map.put("eventID",model.getEventID());

                            if(map.containsKey("imageURL") && map.containsKey("eventID")){
                                uploadData();
                            }
                            else{
                                getEventDetails();
                            }

                    } else{
                        Toast.makeText(ActivityUploadResources.this, "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
                        Log.d("UploadResourcesLogs", "model null");
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("UploadResourcesLogs", databaseError.getDetails());
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }
}