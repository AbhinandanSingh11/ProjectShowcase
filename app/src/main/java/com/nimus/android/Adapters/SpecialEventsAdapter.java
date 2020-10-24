package com.nimus.android.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.nimus.android.AppData.EventsAppData;
import com.nimus.android.Models.ProjectModel;
import com.nimus.android.Models.SpecialEvents;
import com.nimus.android.R;
import com.nimus.android.SpecialEventsDesc;
import com.nimus.android.projectDesc;

import java.util.ArrayList;

public class SpecialEventsAdapter extends RecyclerView.Adapter<SpecialEventsAdapter.ViewHolder> {

    private ArrayList<SpecialEvents> list;
    private Context context;


    public SpecialEventsAdapter(ArrayList<SpecialEvents> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recylerview_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getName());
        holder.price.setText(list.get(position).getStatus());
        Glide.with(context)
                .load(list.get(position).getImage())
                .placeholder(R.color.black)
                .centerCrop()
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventsAppData.getInstance().getSpecialEvents().clear();
                EventsAppData.getInstance().getSpecialEvents().add(list.get(position));
                if(EventsAppData.getInstance().getSpecialEvents().size() != 0){
                    Intent intent = new Intent(context, SpecialEventsDesc.class);
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                }

            }
        });
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
