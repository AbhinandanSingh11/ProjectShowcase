package com.nimus.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.nimus.android.Adapters.RecyclerViewAdapter;
import com.nimus.android.Adapters.SliderPagerAdapter;
import com.nimus.android.AppData.SlideAppData;
import com.nimus.android.AppData.UrlAppData;
import com.nimus.android.AppData.UserAppData;
import com.nimus.android.Models.ProjectModel;
import com.nimus.android.Models.Slide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private CircleImageView circleImageView;
    private ArrayList<ProjectModel> list = new ArrayList<>();
    private AppCompatTextView name;
    private SwipeRefreshLayout refreshLayout;
    private TextView viewAll;
    private FloatingActionButton fab;
    private LinearLayout anim;

    @Override
    public void onBackPressed() {
        finish();
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(getColor(R.color.black));


        recyclerView = findViewById(R.id.recyclerViewMain);
        refreshLayout = findViewById(R.id.refreshMovies);
        viewAll = findViewById(R.id.TextViewViewAllRecentlyReleased);
        circleImageView = findViewById(R.id.ImageViewlogo);
        name = findViewById(R.id.nameMainActivity);
        fab = findViewById(R.id.fab_create);
        anim = findViewById(R.id.animLayoutMain);

        name.setText("Hello " + UserAppData.getInstance().getCurrentUser().getDisplayName());


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityProfile.class);
                startActivity(intent);
            }
        });

        Glide.with(MainActivity.this)
                .load(UserAppData.getInstance().getCurrentUser().getPhotoUrl())
                .placeholder(R.color.black)
                .into(circleImageView);


        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecentyReleasedActivity.class);
                startActivity(intent);

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UploadActivity.class));
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                adapter.notifyDataSetChanged();
                new Sync().execute();

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

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(13), false));


        new Sync().execute();

        loadRecyclerView();
    }

    void loadRecyclerView(){
        adapter = new RecyclerViewAdapter(list,MainActivity.this);
        RecyclerView.LayoutManager manager = new GridLayoutManager(MainActivity.this,2);
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

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private class Sync extends AsyncTask<Void, Void, Void > {
        @Override
        protected void onPreExecute() {
            refreshLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            fetchData();
            return null;
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        SlideAppData.getInstance().getArrayList().clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SlideAppData.getInstance().getArrayList().clear();
    }

    void hide(){
        viewAll.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.GONE);
    }

    void show(){
        viewAll.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        refreshLayout.setVisibility(View.VISIBLE);
    }

    void fetchData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Projects");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ProjectModel model = snapshot.getValue(ProjectModel.class);
                    list.add(model);
                    anim.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                }

                if(list.size()<=0){
                    refreshLayout.setRefreshing(false);
                    Toast.makeText(MainActivity.this, "No Projects Available", Toast.LENGTH_SHORT).show();
                    anim.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Database Error reading: ", databaseError.getDetails());
                refreshLayout.setRefreshing(false);
                hide();
            }
        });

        show();
    }
}
