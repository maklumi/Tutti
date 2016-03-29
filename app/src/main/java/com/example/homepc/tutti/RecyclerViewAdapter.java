package com.example.homepc.tutti;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Maklumi on 28-03-16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.ViewHolder>{

    ClickListener clickListener;
    ArrayList<ItemsModel> itemsModels = null;
    Context context;

    public RecyclerViewAdapter(ArrayList<ItemsModel> itemsModels, Context context) {
        this.itemsModels = itemsModels;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(itemsModels.get(position).getTitle());
        holder.date.setText(itemsModels.get(position).getPrice());
        holder.price.setText(itemsModels.get(position).getPrice());
        Picasso.with(context).load(itemsModels.get(position).getImageFile()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return itemsModels.size();
    }

    public void updateData(ArrayList<ItemsModel> itemsModels) {
        this.itemsModels = itemsModels;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title, price, date;
        public ImageView imageView;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            view = itemView;
            title= (TextView)view.findViewById(R.id.adTitle);
            price= (TextView)view.findViewById(R.id.adPrice);
            date = (TextView)view.findViewById(R.id.adDate);
            imageView= (ImageView) view.findViewById(R.id.adPic);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface ClickListener{
        public void onItemClick(View view, int position);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
