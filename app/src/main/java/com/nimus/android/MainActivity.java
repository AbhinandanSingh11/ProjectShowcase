package com.nimus.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

    private ViewPager sliderpager;
    private TabLayout indicator;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private SliderPagerAdapter slideadapter;
    private ProgressBar progressSlide;
    private CircleImageView circleImageView;
    private ArrayList<ProjectModel> list = new ArrayList<>();
    private final String SlideURL = "https://www.nimus.co.in/nimus/slides.json";
    private final String URL = "https://www.nimus.co.in/nimus/post.json";
    private JsonArrayRequest request, requestSlide;
    private RequestQueue queue;
    private AppCompatTextView name;
    private SwipeRefreshLayout refreshLayout;
    private TextView viewAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(getColor(R.color.black));

        sliderpager = findViewById(R.id.slider_pager) ;
        indicator = findViewById(R.id.indicator);
        recyclerView = findViewById(R.id.recyclerViewMain);
        refreshLayout = findViewById(R.id.refreshMovies);
        viewAll = findViewById(R.id.TextViewViewAllRecentlyReleased);
        progressSlide = findViewById(R.id.progressSlides);
        circleImageView = findViewById(R.id.ImageViewlogo);
        name = findViewById(R.id.nameMainActivity);

        name.setText("Hello "+ UserAppData.getInstance().getCurrentUser().getDisplayName());


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ActivityProfile.class);
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
                Intent intent = new Intent(MainActivity.this,RecentyReleasedActivity.class);
                startActivity(intent);

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                adapter.notifyDataSetChanged();
                queue.add(request);
                new Sync().execute(URL);

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

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(13),false));




        queue = Volley.newRequestQueue(MainActivity.this);
        new Sync().execute(URL);


        loadSlides();
        loadRecyclerView();
    }

    void loadSlides(){
        slideadapter = new SliderPagerAdapter(this);
        sliderpager.setAdapter(slideadapter);

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if(sliderpager.getCurrentItem() == SlideAppData.getInstance().getArrayList().size()-1){
                    sliderpager.setCurrentItem(0);
                }

                else{
                    sliderpager.setCurrentItem(sliderpager.getCurrentItem()+1,true);
                }
            }
        };

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        },4000,6000);

        indicator.setupWithViewPager(sliderpager,true);
    }

    void loadRecyclerView(){
        adapter = new RecyclerViewAdapter(list,MainActivity.this);
        RecyclerView.LayoutManager manager = new GridLayoutManager(MainActivity.this,2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    public void fetch(String url){
        request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for( int i = 0; i<response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        list.add(new ProjectModel(object.getString("title"),object.getString("price"),object.getString("image_link"),object.getString("publish_date"),object.getString("desc"),object.getString("size"),object.getString("author"),object.getString("co_author"),object.getString("projectURL"),object.getString("instagramPostURL")));
                        refreshLayout.setRefreshing(false);
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        refreshLayout.setRefreshing(false);
                        hide();
                    }
                }

                show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }

                refreshLayout.setRefreshing(false);
                hide();

            }
        });
        queue.add(request);
    }

    public void fetchSlide(String url){
        requestSlide = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for( int i = 0; i<response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        SlideAppData.getInstance().getArrayList().add(new Slide(object.getString("url"),object.getString("link")));
                        progressSlide.setVisibility(View.GONE);
                        slideadapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressSlide.setVisibility(View.GONE);
                        hide();
                    }
                }

                show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String message = null;
                if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }

                progressSlide.setVisibility(View.GONE);
                hide();
            }
        });
        queue.add(requestSlide);
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


    private class Sync extends AsyncTask<String, Void, Void > {
        @Override
        protected void onPreExecute() {
            refreshLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(String... strings) {
            fetch(strings[0]);
            return null;
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        SlideAppData.getInstance().getArrayList().clear();
        fetchSlide(SlideURL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SlideAppData.getInstance().getArrayList().clear();
    }

    void hide(){
        viewAll.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        sliderpager.setVisibility(View.GONE);
        indicator.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.GONE);
        progressSlide.setVisibility(View.GONE);
    }

    void show(){
        viewAll.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        sliderpager.setVisibility(View.VISIBLE);
        indicator.setVisibility(View.VISIBLE);
        refreshLayout.setVisibility(View.VISIBLE);
    }
}
