package com.nimus.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.nimus.android.AppData.AppDataModel;
import com.nimus.android.Models.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisitUser extends AppCompatActivity implements View.OnClickListener {

    private ImageView mEmail, mInsta, mLink, mGit,mBioP,mCurrentPos,back;
    private String insta,linkedin,git,email;
    private CircleImageView profileImage;
    private TextView name,mCurrentPosition,mBio;
    private String uid, username, imageURL;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_user);

        profileImage = findViewById(R.id.photoVisitProfile);
        mEmail = findViewById(R.id.userMail);
        mInsta = findViewById(R.id.userInsta);
        mLink = findViewById(R.id.userLink);
        mGit = findViewById(R.id.userGit);
        mCurrentPosition = findViewById(R.id.textCurrentPos);
        mBio = findViewById(R.id.textBio);
        name = findViewById(R.id.textName);
        mBioP = findViewById(R.id.imageBioUser);
        mCurrentPos = findViewById(R.id.imageCurrentPos);
        back = findViewById(R.id.backViewProfile);

        mEmail.setOnClickListener(this);
        mInsta.setOnClickListener(this);
        mLink.setOnClickListener(this);
        mGit.setOnClickListener(this);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            user = FirebaseAuth.getInstance().getCurrentUser();
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        setDetails();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisitUser.super.onBackPressed();
            }
        });
    }


    private void setDetails(){

        reference = FirebaseDatabase.getInstance().getReference("users").child(AppDataModel.getInstance().getArrayList().get(0).getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user!=null){

                    if(user.getInstagram()!=null){
                        mInsta.setVisibility(View.VISIBLE);
                        insta = user.getInstagram();
                    }else{
                        mInsta.setVisibility(View.GONE);
                    }

                    if(user.getLinkedin()!=null){
                        mLink.setVisibility(View.VISIBLE);
                        linkedin = user.getLinkedin();
                    }else{
                        mLink.setVisibility(View.GONE);
                    }

                    if(user.getGithub()!=null){
                        mGit.setVisibility(View.VISIBLE);
                        git = user.getGithub();
                    }else{
                        mGit.setVisibility(View.GONE);
                    }

                    if(user.getCurrentPosition()!=null){
                        mCurrentPosition.setVisibility(View.VISIBLE);
                        mCurrentPos.setVisibility(View.VISIBLE);
                        mCurrentPosition.setText(user.getCurrentPosition());
                    }else{
                        mCurrentPosition.setVisibility(View.GONE);
                        mCurrentPos.setVisibility(View.GONE);
                    }

                    if(user.getBio()!=null){
                        mBio.setVisibility(View.VISIBLE);
                        mBioP.setVisibility(View.VISIBLE);
                        mBio.setText(user.getBio());
                    }else{
                        mBio.setVisibility(View.GONE);
                        mBioP.setVisibility(View.GONE);
                    }

                    if(user.getName()!=null){
                        name.setText(user.getName());
                        Glide.with(VisitUser.this)
                                .load(user.getImage())
                                .placeholder(R.color.white)
                                .into(profileImage);
                    }
                    if(user.getEmail()!=null){
                        email = user.getEmail();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.userMail: {
                url = email;
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={url};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
                break;
            }

            case R.id.userInsta: {
                url = insta;
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "No application can handle this request."
                            + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            }

            case R.id.userLink: {
                url = linkedin;
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "No application can handle this request."
                            + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            }

            case R.id.userGit: {
                url = git;
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "No application can handle this request."
                            + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}