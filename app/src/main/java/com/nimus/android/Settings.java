package com.nimus.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nimus.android.Models.User;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {
    private EditText mInstagram, mLinkedIn, mGithub, mCurrentPosition, mBio;
    private ImageView back;
    private TextView save;
    private String instagram, linkedin, github, currentPosition, bio;
    private DatabaseReference reference;
    private String uid;
    private CardView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mInstagram = findViewById(R.id.et_instagram);
        mLinkedIn = findViewById(R.id.et_linkedin);
        mGithub = findViewById(R.id.et_github);
        mCurrentPosition = findViewById(R.id.et_current_pos);
        mBio = findViewById(R.id.et_bio);
        save = findViewById(R.id.saveSettings);
        back = findViewById(R.id.backSettings);
        progress = findViewById(R.id.progressSave);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        getData();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.super.onBackPressed();
            }
        });
    }


    private void getData(){
        progress.setVisibility(View.VISIBLE);
        reference = FirebaseDatabase.getInstance().getReference("users").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user!=null){

                    if(user.getInstagram()!=null){
                        mInstagram.setText(user.getInstagram());
                    }else{
                        mInstagram.setHint("Add Instagram");
                    }

                    if(user.getLinkedin()!=null){
                        mLinkedIn.setText(user.getLinkedin());
                    }else{
                        mLinkedIn.setHint("Add LinkedIn");
                    }

                    if(user.getGithub()!=null){
                        mGithub.setText(user.getGithub());
                    }else{
                        mGithub.setHint("Add Github");
                    }

                    if(user.getCurrentPosition()!=null){
                        mCurrentPosition.setText(user.getCurrentPosition());
                    }else{
                        mCurrentPosition.setHint("Add Current position");
                    }

                    if(user.getBio()!=null){
                        mBio.setText(user.getBio());
                    }else{
                        mBio.setHint("Add Bio");
                    }

                    progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progress.setVisibility(View.GONE);
            }
        });
    }

    private void saveData(){
        progress.setVisibility(View.VISIBLE);
        instagram = mInstagram.getText().toString().trim();
        linkedin = mLinkedIn.getText().toString().trim();
        github = mGithub.getText().toString().trim();
        currentPosition = mCurrentPosition.getText().toString().trim();
        bio = mBio.getText().toString().trim();

        Log.d("sbfgsdlfiifu",instagram);
        Log.d("sbfgsdlfiifu",linkedin);
        Log.d("sbfgsdlfiifu",github);
        Log.d("sbfgsdlfiifu",currentPosition);
        Log.d("sbfgsdlfiifu",bio);

        if(!instagram.isEmpty()){
            // Saving Instagram Link
            reference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("instagram");
            reference.setValue(instagram).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }
                    else{
                        Toast.makeText(Settings.this, "Instagram not updated, try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            reference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("instagram");
            reference.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }
                    else{
                        Toast.makeText(Settings.this, "Instagram not updated, try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if(!linkedin.isEmpty()){
            //Saving LinkedIn
            reference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("linkedin");
            reference.setValue(linkedin).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }
                    else{
                        Toast.makeText(Settings.this, "LinkedIn not updated, try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            reference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("linkedin");
            reference.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }
                    else{
                        Toast.makeText(Settings.this, "LinkedIn not updated, try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if(!github.isEmpty()){
            //Saving Github
            reference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("github");
            reference.setValue(github).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }
                    else{
                        Toast.makeText(Settings.this, "Github not updated, try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            reference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("github");
            reference.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }
                    else{
                        Toast.makeText(Settings.this, "Github not updated, try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if(!currentPosition.isEmpty()){
            //Saving currentPosition
            reference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("currentPosition");
            reference.setValue(currentPosition).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }
                    else{
                        Toast.makeText(Settings.this, "Current position not updated, try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            reference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("currentPosition");
            reference.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }
                    else{
                        Toast.makeText(Settings.this, "Current position not updated, try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if(!bio.isEmpty()){
            //Saving Bio
            reference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("bio");
            reference.setValue(bio).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }
                    else{
                        Toast.makeText(Settings.this, "Bio not updated, try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            reference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("bio");
            reference.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }
                    else{
                        Toast.makeText(Settings.this, "Bio not updated, try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        progress.setVisibility(View.GONE);
    }
}