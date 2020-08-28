package com.nimus.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nimus.android.AppData.AppDataModel;
import com.nimus.android.Models.ProjectModel;
import com.nimus.android.R;
import com.nimus.android.projectDesc;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<ProjectModel> list;
    private Context context;


    public RecyclerViewAdapter(ArrayList<ProjectModel> list, Context context) {
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
        holder.title.setText(list.get(position).getTitle());
        holder.price.setText(list.get(position).getPrice());
        Glide.with(context)
                .load(list.get(position).getDisplayImageURL())
                .placeholder(R.color.black)
                .centerCrop()
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDataModel.getInstance().getArrayList().clear();
                AppDataModel.getInstance().getArrayList().add(list.get(position));
                Intent intent = new Intent(context, projectDesc.class);
                context.startActivity(intent);
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
