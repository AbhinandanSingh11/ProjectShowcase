package com.nimus.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nimus.android.AppData.UserAppData;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityProfile extends AppCompatActivity {

    private ImageView back;
    private CircleImageView image;
    private TextView name,email;
    private LinearLayout myProjects,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getWindow().setStatusBarColor(getResources().getColor(R.color.black,getTheme()));

        back = findViewById(R.id.backProfile);
        image = findViewById(R.id.imageUserProfile);
        name = findViewById(R.id.nameProfile);
        email = findViewById(R.id.emailProfile);
        myProjects = findViewById(R.id.layoutMyProjects);
        logout = findViewById(R.id.layoutLogout);


        myProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityProfile.this,MyProjectsAcivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ActivityProfile.this,LoginActivity.class));
                finish();
                finishAffinity();
            }
        });

        updateUI(UserAppData.getInstance().getCurrentUser());



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityProfile.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    void updateUI(FirebaseUser user){
        if(user !=  null){
            Glide.with(ActivityProfile.this)
                    .load(user.getPhotoUrl())
                    .placeholder(R.drawable.logo)
                    .into(image);

            email.setText(user.getEmail());
            name.setText(user.getDisplayName());
        }
    }
}
