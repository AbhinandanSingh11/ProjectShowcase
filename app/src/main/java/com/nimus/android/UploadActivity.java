package com.nimus.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 100;

    private EditText title,category,desc,url;
    private Button upload,select;
    private ImageView back;
    private String titleText, categoryText, descText, urlText;
    private String name, email, uid, image;
    private String date;
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
        select = findViewById(R.id.button_select_project);
        back = findViewById(R.id.backUpload);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Projects");

        title.setOnClickListener(this);
        category.setOnClickListener(this);
        desc.setOnClickListener(this);
        url.setOnClickListener(this);
        upload.setOnClickListener(this);
        select.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_upload_project:{
                getUserInput();
                getCurrentUserDetails();
                getFormattedDate();
                uploadData();
                break;

            }

            case R.id.button_select_project:{
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
                break;
            }

            case R.id.backUpload:{
                startActivity(new Intent(UploadActivity.this,MainActivity.class));
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
            map.put("cost","free");
            map.put("date",date);

            if(map.containsKey("imageURL")){



                databaseReference.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(UploadActivity.this, "Thanks For Contributing to DSC CU", Toast.LENGTH_SHORT).show();
                            title.setText(null);
                            category.setText(null);
                            desc.setText(null);
                            url.setText(null);

                            upload.setVisibility(View.GONE);
                            select.setVisibility(View.VISIBLE);
                        }
                        else{
                            Toast.makeText(UploadActivity.this, "Failed to upload!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                Toast.makeText(this, "Please Upload the Image again!!", Toast.LENGTH_SHORT).show();
                select.setVisibility(View.VISIBLE);
                upload.setVisibility(View.GONE);
            }


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

    void getFormattedDate(){
        Date mDate= Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
        date = dateFormat.format(mDate);
    }

    private void uploadImage(Uri uri){
        String filename = Double.toString(Math.random() * 10000);

        final StorageReference storageReference = FirebaseStorage.getInstance().getReference("projectImage");
        final StorageReference userReference = storageReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        final StorageReference imageReference = userReference.child(filename);

        final UploadTask uploadTask = imageReference.putFile(uri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        map.put("imageURL",uri.toString());
                        select.setVisibility(View.GONE);
                        upload.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadActivity.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                        select.setVisibility(View.VISIBLE);
                        upload.setVisibility(View.GONE);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, "Failed to upload image!", Toast.LENGTH_SHORT).show();
                select.setVisibility(View.VISIBLE);
                upload.setVisibility(View.GONE);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                double progress = Math.round(100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                Toast.makeText(UploadActivity.this, "Uploading...." + progress + "%", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            uploadImage(selectedImage);
        }
    }
}