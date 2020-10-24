package com.nimus.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.nimus.android.AppData.UrlAppData;
import com.nimus.android.AppData.UrlData;
import com.nimus.android.AppData.UserAppData;
import com.nimus.android.Models.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityProfile extends AppCompatActivity {

    private ImageView back;
    private CircleImageView image;
    private TextView name,email;
    private LinearLayout myProjects,logout,settings,feedback,privacy,ads,specialEvents,rate,update,share,aboutDSCCU,version;
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
        specialEvents = findViewById(R.id.layoutSpecialEvents);
        rate = findViewById(R.id.layoutLike);
        share = findViewById(R.id.layoutShare);
        update = findViewById(R.id.layoutUpdate);
        aboutDSCCU = findViewById(R.id.layoutAboutDSC);
        version = findViewById(R.id.layoutVersion);


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
                /*UrlData.getInstance();
                if(UrlData.getUrl()!=null){
                    Intent intent = new Intent(ActivityProfile.this,Browser.class);
                    intent.putExtra("URL",UrlData.getUrl().getAds());
                    startActivity(intent);
                }*/

                Toast.makeText(ActivityProfile.this, "Disabled!", Toast.LENGTH_SHORT).show();
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

                new IOSDialog.Builder(ActivityProfile.this)
                        .setTitle("Alert!")
                        .setMessage("Do you really want to logout of this application?")
                        .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(ActivityProfile.this,LoginActivity.class));
                        finish();
                        finishAffinity();
                    }
                }).setCancelable(false)
                        .show();
            }
        });

        specialEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityProfile.this,SpecialEvents.class));
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UrlData.getInstance();
                if(UrlData.getUrl()!=null){
                    Intent intent = new Intent(ActivityProfile.this,Browser.class);
                    intent.putExtra("URL",UrlData.getUrl().getRatingURL());
                    startActivity(intent);
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "DSC CU");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + UrlData.getUrl().getShareURL();
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(UrlData.getUrl().getUpdate()));
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ActivityProfile.this, "No application can handle this request."
                            + " Please install a web browser",  Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        aboutDSCCU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://dsc.community.dev/chandigarh-university"));
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ActivityProfile.this, "No application can handle this request."
                            + " Please install a web browser",  Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityProfile.this, "Version: 1.0.1", Toast.LENGTH_SHORT).show();
            }
        });

        updateUI(UserAppData.getInstance().getCurrentUser());



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityProfile.super.onBackPressed();
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
