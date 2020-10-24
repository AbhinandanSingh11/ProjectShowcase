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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nimus.android.Adapters.RecyclerViewAdapter;
import com.nimus.android.Adapters.SpecialEventsAdapter;
import com.nimus.android.Models.ProjectModel;

import java.util.ArrayList;

public class SpecialEvents extends AppCompatActivity {
    private ImageView back;
    private RecyclerView recyclerView;
    private SpecialEventsAdapter adapter;
    private ArrayList<com.nimus.android.Models.SpecialEvents> listSpecialEvents = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_events);

        getWindow().setStatusBarColor(getResources().getColor(R.color.black,getTheme()));

        back  = findViewById(R.id.ImageViewBackSpecialEvents);
        recyclerView = findViewById(R.id.recyclerViewSpecialEvents);
        refreshLayout = findViewById(R.id.refreshSpecialEvents);
        anim = findViewById(R.id.animLayoutSE);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listSpecialEvents.clear();
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

        recyclerView.addItemDecoration(new SpecialEvents.GridSpacingItemDecoration(2, dpToPx(15),false));



        fetchData();
        loadRecyclerView();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpecialEvents.super.onBackPressed();
            }
        });
    }

    void loadRecyclerView(){
        adapter = new SpecialEventsAdapter(listSpecialEvents,SpecialEvents.this);
        RecyclerView.LayoutManager manager = new GridLayoutManager(SpecialEvents.this,2);
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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("specialEvents");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    com.nimus.android.Models.SpecialEvents model = snapshot.getValue(com.nimus.android.Models.SpecialEvents.class);
                    listSpecialEvents.add(model);
                    anim.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                }

                if(listSpecialEvents.size()<=0){
                    refreshLayout.setRefreshing(false);
                    Toast.makeText(SpecialEvents.this, "No Special Events found", Toast.LENGTH_SHORT).show();
                    anim.setVisibility(View.VISIBLE);
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
