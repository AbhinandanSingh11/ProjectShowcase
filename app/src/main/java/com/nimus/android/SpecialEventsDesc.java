package com.nimus.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nimus.android.AppData.AppDataModel;
import com.nimus.android.AppData.EventsAppData;

public class SpecialEventsDesc extends AppCompatActivity {
    private ImageView back;
    private Button eventResources;
    private ImageView imageView,permaLink;
    private TextView startDate,title,description,endDate,status,eventDomain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_events_desc);

        getWindow().setStatusBarColor(getResources().getColor(R.color.black,getTheme()));

        back = findViewById(R.id.BackImageEventsDesc);
        imageView = findViewById(R.id.imageEventsDesc);
        description = findViewById(R.id.textViewEventDescription);
        startDate =  findViewById(R.id.textViewEventsAuthor);
        title = findViewById(R.id.textViewEventsTitle);
        endDate = findViewById(R.id.textViewEventsDate);
        eventDomain = findViewById(R.id.Eventdomain);
        status = findViewById(R.id.textViewEventPrice);
        eventResources = findViewById(R.id.ButtonEventResources);
        back = findViewById(R.id.BackImageEventsDesc);
        permaLink = findViewById(R.id.eventDescPermalink);



        description.setText(EventsAppData.getInstance().getSpecialEvents().get(0).getDescription());
        title.setText(EventsAppData.getInstance().getSpecialEvents().get(0).getName());
        startDate.setText(EventsAppData.getInstance().getSpecialEvents().get(0).getStartDate());
        endDate.setText(EventsAppData.getInstance().getSpecialEvents().get(0).getEndDate());
        eventDomain.setText(EventsAppData.getInstance().getSpecialEvents().get(0).getDomain());
        status.setText(EventsAppData.getInstance().getSpecialEvents().get(0).getStatus());
        Glide.with(SpecialEventsDesc.this)
                .load(EventsAppData.getInstance().getSpecialEvents().get(0).getImage())
                .placeholder(R.color.black)
                .centerCrop()
                .into(imageView);

        eventResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SpecialEventsDesc.this, EventResources.class));
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpecialEventsDesc.super.onBackPressed();
            }
        });

        permaLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(EventsAppData.getInstance().getSpecialEvents().get(0).getEventPermaLink()));
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(SpecialEventsDesc.this, "No application can handle this request."
                            + " Please install a web browser",  Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }
}