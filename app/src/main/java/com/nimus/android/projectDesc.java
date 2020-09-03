package com.nimus.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.nimus.android.AppData.AppDataModel;

import java.util.Objects;

public class projectDesc extends AppCompatActivity {

    ImageView back;
    private RewardedAd rewardedAd;
    Button viewAd, viewOptions;
    TextView author,title,description,date,price,co_author;
    ImageView imageView;
    RewardedAdLoadCallback adLoadCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_desc);

        getWindow().setStatusBarColor(getResources().getColor(R.color.black,getTheme()));

        back = findViewById(R.id.BackImageDesc);
        viewAd = findViewById(R.id.ButtonAd);
        viewOptions = findViewById(R.id.ButtonView);
        author =  findViewById(R.id.textViewAuthor);
        title = findViewById(R.id.textViewTitle);
        description = findViewById(R.id.textViewDescription);
        date = findViewById(R.id.textViewDate);
        price = findViewById(R.id.textViewPrice);
        imageView = findViewById(R.id.imageDesc);
        co_author = findViewById(R.id.co_author);

        author.setText(AppDataModel.getInstance().getArrayList().get(0).getUser());
        title.setText(AppDataModel.getInstance().getArrayList().get(0).getTitle());
        description.setText(AppDataModel.getInstance().getArrayList().get(0).getDesc());
        date.setText(AppDataModel.getInstance().getArrayList().get(0).getDate());
        price.setText(AppDataModel.getInstance().getArrayList().get(0).getCost());
        co_author.setText(AppDataModel.getInstance().getArrayList().get(0).getCategory());
        Glide.with(projectDesc.this)
                .load(AppDataModel.getInstance().getArrayList().get(0).getImageURL())
                .placeholder(R.color.black)
                .centerCrop()
                .into(imageView);

        viewAd.setText("Loading...");
        viewAd.setTextColor(getResources().getColor(R.color.grey_500,getTheme()));
        viewAd.setBackground(getResources().getDrawable(R.drawable.button_loading,getTheme()));

        rewardedAd = new  RewardedAd(this,"ca-app-pub-3940256099942544/5224354917");

        adLoadCallback = new RewardedAdLoadCallback(){
            @Override
            public void onRewardedAdLoaded() {
                super.onRewardedAdLoaded();
                viewAd.setText("Watch a Video");
                viewAd.setTextColor(getResources().getColor(R.color.white,getTheme()));
                viewAd.setBackground(getResources().getDrawable(R.drawable.button_bright_green,getTheme()));
            }

            @Override
            public void onRewardedAdFailedToLoad(int i) {
                super.onRewardedAdFailedToLoad(i);
                Toast.makeText(projectDesc.this, "Ad loading failed "+i, Toast.LENGTH_SHORT).show();
            }
        };

        rewardedAd.loadAd(new AdRequest.Builder().build(),adLoadCallback);


        viewAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity activity = projectDesc.this;
                if(rewardedAd.isLoaded()){
                    RewardedAdCallback rewardedAdCallback = new RewardedAdCallback(){
                        boolean state;
                        @Override
                        public void onRewardedAdOpened() {
                            super.onRewardedAdOpened();
                            Toast.makeText(projectDesc.this, "Ad covered the screen", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            super.onRewardedAdClosed();
                            if(!state) {
                                Toast.makeText(projectDesc.this, "Ad was closed, reward not earned", Toast.LENGTH_SHORT).show();
                                viewAd.setText("Loading...");
                                viewAd.setTextColor(getResources().getColor(R.color.grey_500,getTheme()));
                                viewAd.setBackground(getResources().getDrawable(R.drawable.button_loading,getTheme()));
                                projectDesc.this.rewardedAd = createAndLoadRewardedAd();
                            }
                        }

                        @Override
                        public void onRewardedAdFailedToShow(int i) {
                            super.onRewardedAdFailedToShow(i);
                            Toast.makeText(projectDesc.this, "Ad failed to show", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            state = true;
                            Toast.makeText(projectDesc.this, "User earned the reward", Toast.LENGTH_SHORT).show();
                            viewAd.setVisibility(View.GONE);
                            viewOptions.setVisibility(View.VISIBLE);
                        }
                    };
                    rewardedAd.show(activity,rewardedAdCallback);
                }
                else{
                    Toast.makeText(activity, "Ad wasn't loaded properly", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(projectDesc.this, "Redirect", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(projectDesc.this,Browser.class);
                intent.putExtra("URL", AppDataModel.getInstance().getArrayList().get(0).getUrl());
                intent.putExtra("code",1);
                startActivity(intent);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(projectDesc.this,MainActivity.class);
                startActivity(intent);
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        rewardedAd.loadAd(new AdRequest.Builder().build(),adLoadCallback);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        rewardedAd.loadAd(new AdRequest.Builder().build(),adLoadCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rewardedAd.loadAd(new AdRequest.Builder().build(),adLoadCallback);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        rewardedAd.loadAd(new AdRequest.Builder().build(),adLoadCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        rewardedAd.loadAd(new AdRequest.Builder().build(),adLoadCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rewardedAd.loadAd(new AdRequest.Builder().build(),adLoadCallback);
    }

    private RewardedAd createAndLoadRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917");
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                viewAd.setText("Watch a Video");
                viewAd.setTextColor(getResources().getColor(R.color.white,getTheme()));
                viewAd.setBackground(getResources().getDrawable(R.drawable.button_bright_green,getTheme()));
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }
}
