package com.nimus.android.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nimus.android.AppData.AppDataModel;
import com.nimus.android.AppData.EventResourceAppData;
import com.nimus.android.AppData.EventsAppData;
import com.nimus.android.Models.EventResource;
import com.nimus.android.Models.ProjectModel;
import com.nimus.android.Models.SpecialEvents;
import com.nimus.android.R;
import com.nimus.android.SpecialEventsDesc;
import com.nimus.android.projectDesc;

import java.util.ArrayList;

public class EventResourcesAdapter extends RecyclerView.Adapter<EventResourcesAdapter.ViewHolder> {

    private ArrayList<EventResource> list;
    private Context context;


    public EventResourcesAdapter(ArrayList<EventResource> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public EventResourcesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventResourcesAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.recylerview_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.title.setText(list.get(i).getTitle());
        viewHolder.price.setText(list.get(i).getResType());
        Glide.with(context)
                .load(EventsAppData.getInstance().getSpecialEvents().get(0).getImage())
                .placeholder(R.color.black)
                .centerCrop()
                .into(viewHolder.image);

        if (list.get(i).getType() == 0){
            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //open in associated Application
                    String url = list.get(i).getUrl();
                    try {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(myIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "No application can handle this request."
                                + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });
        }
        else{
            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProjectModel projectModel = new ProjectModel();
                    projectModel.setCategory(list.get(i).getCategory());
                    projectModel.setCost(list.get(i).getCost());
                    projectModel.setDate(list.get(i).getDate());
                    projectModel.setDesc(list.get(i).getDesc());
                    projectModel.setEmail(list.get(i).getEmail());
                    projectModel.setImageURL(list.get(i).getImageURL());
                    projectModel.setTitle(list.get(i).getTitle());
                    projectModel.setUid(list.get(i).getUid());
                    projectModel.setUrl(list.get(i).getUrl());
                    projectModel.setUser(list.get(i).getUser());
                    AppDataModel.getInstance().getArrayList().clear();
                    AppDataModel.getInstance().getArrayList().add(projectModel);
                    Intent intent = new Intent(context, projectDesc.class);
                    context.startActivity(intent);
                }
            });
        }


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,price;
        ImageView image;
        LinearLayout layout;
        RelativeLayout parentCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            price = itemView.findViewById(R.id.textViewItemPrice);
            title = itemView.findViewById(R.id.textViewItemTitle);
            image = itemView.findViewById(R.id.imageViewItemImage);
            layout = itemView.findViewById(R.id.parentLayout);
            parentCard = itemView.findViewById(R.id.parent);

        }
    }
}
