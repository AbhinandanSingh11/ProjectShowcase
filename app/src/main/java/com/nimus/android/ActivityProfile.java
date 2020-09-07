package com.nimus.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nimus.android.AppData.UrlAppData;
import com.nimus.android.AppData.UrlData;
import com.nimus.android.AppData.UserAppData;
import com.nimus.android.Models.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityProfile extends AppCompatActivity {

    private ImageView back;
    private CircleImageView image;
    private TextView name,email;
    private LinearLayout myProjects,logout,settings,feedback,privacy,ads;
    private DatabaseReference reference;


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
        settings = findViewById(R.id.layoutSettings);
        feedback = findViewById(R.id.layoutFeedback);
        privacy = findViewById(R.id.layoutPrivacy);
        ads = findViewById(R.id.layoutAd);

        getData();

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UrlData.getInstance();
                if(UrlData.getUrl()!=null){
                    Intent intent = new Intent(ActivityProfile.this,Browser.class);
                    intent.putExtra("URL",UrlData.getUrl().getFeedback());
                    startActivity(intent);
                }
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UrlData.getInstance();
                if(UrlData.getUrl()!=null){
                    Intent intent = new Intent(ActivityProfile.this,Browser.class);
                    intent.putExtra("URL",UrlData.getUrl().getPrivacy_policy());
                    startActivity(intent);
                }
            }
        });

        ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UrlData.getInstance();
                if(UrlData.getUrl()!=null){
                    Intent intent = new Intent(ActivityProfile.this,Browser.class);
                    intent.putExtra("URL",UrlData.getUrl().getAds());
                    startActivity(intent);
                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityProfile.this,Settings.class));
            }
        });

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

    private void getData(){
        reference = FirebaseDatabase.getInstance().getReference("url");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UrlData.getInstance();
                UrlData.setUrl(dataSnapshot.getValue(URL.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
