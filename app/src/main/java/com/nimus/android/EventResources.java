package com.nimus.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nimus.android.Adapters.EventResourcesAdapter;
import com.nimus.android.Adapters.RecyclerViewAdapter;
import com.nimus.android.AppData.EventsAppData;
import com.nimus.android.Models.EventResource;
import com.nimus.android.Models.ProjectModel;

import java.util.ArrayList;

public class EventResources extends AppCompatActivity {

    private ImageView back;
    private RecyclerView recyclerView;
    private EventResourcesAdapter adapter;
    private ArrayList<EventResource> list = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout animationView;
    private FloatingActionButton uploadResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_resources);
        getWindow().setStatusBarColor(getResources().getColor(R.color.black,getTheme()));

        back  = findViewById(R.id.ImageViewBackEventResources);
        recyclerView = findViewById(R.id.recyclerViewEventResources);
        refreshLayout = findViewById(R.id.refreshEventResources);
        animationView = findViewById(R.id.animLayoutER);
        uploadResources = findViewById(R.id.fab_addResources);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                adapter.notifyDataSetChanged();
                fetchData();
            }
        });
        refreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });

        recyclerView.addItemDecoration(new EventResources.GridSpacingItemDecoration(2, dpToPx(15),false));



        fetchData();
        loadRecyclerView();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventResources.super.onBackPressed();
            }
        });

        uploadResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventResources.this,ActivityUploadResources.class);
                startActivity(intent);
            }
        });
    }

    void loadRecyclerView(){
        adapter = new EventResourcesAdapter(list,EventResources.this);
        RecyclerView.LayoutManager manager = new GridLayoutManager(EventResources.this,2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    void fetchData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("eventResources");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    EventResource model = snapshot.getValue(EventResource.class);

                    if(model.getEventID().equals(EventsAppData.getInstance().getSpecialEvents().get(0).getEventID())){
                        list.add(model);
                        animationView.setVisibility(View.GONE);
                        refreshLayout.setRefreshing(false);
                        adapter.notifyDataSetChanged();
                    }
                }

                if(list.size()<=0){
                    refreshLayout.setRefreshing(false);
                    Toast.makeText(EventResources.this, "No Event Resources Available", Toast.LENGTH_SHORT).show();
                    animationView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Database Error reading: ", databaseError.getDetails());
                refreshLayout.setRefreshing(false);
            }
        });

    }
}