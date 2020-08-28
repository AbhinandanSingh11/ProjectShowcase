package com.nimus.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.nimus.android.AppData.SlideAppData;
import com.nimus.android.Browser;
import com.nimus.android.R;

import java.util.List;

public class SliderPagerAdapter extends PagerAdapter {

    private Context mContext ;


    public SliderPagerAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {


        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View slideLayout = inflater.inflate(R.layout.slide_item,null);

        slideLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = SlideAppData.getInstance().getArrayList().get(position).getURL();

                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(browserIntent);
                Toast.makeText(mContext, url, Toast.LENGTH_SHORT).show();

            }
        });

        ImageView slideImg = slideLayout.findViewById(R.id.slide_img);

        Glide.with(mContext)
                .load(SlideAppData.getInstance().getArrayList().get(position).getImage())
                .placeholder(R.color.black)
                .into(slideImg);

        container.addView(slideLayout);
        return slideLayout;






    }

    @Override
    public int getCount() {
        return SlideAppData.getInstance().getArrayList().size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}