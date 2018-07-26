package com.wat.pqdmag.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wat.pqdmag.R;
import com.wat.pqdmag.data.Mag;

import java.util.List;

/**
 * Created by Mouayed on 14/09/2016.
 */
public class MagAdapter extends RecyclerView.Adapter<MagAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView thumb;
        private TextView tvDate;

        private ViewHolder(View itemView) {
            super(itemView);

            thumb = (ImageView) itemView.findViewById(R.id.item_mag_thumb);
            tvDate = (TextView) itemView.findViewById(R.id.item_mag_date);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }

    private List<Mag> mMags;
    private Context mContext;
    private static ClickListener clickListener;

    // Pass in the contact array into the constructor
    public MagAdapter(Context context, List<Mag> mags) {
        mMags = mags;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public MagAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.pqd_item_mag, parent, false);

        return new ViewHolder(itemView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(MagAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Mag mag = mMags.get(position);

        ImageView imageView = viewHolder.thumb;
        imageView.setImageResource(mag.getThumb());
        TextView textView = viewHolder.tvDate;
        textView.setText(mag.getDate());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mMags.size();
    }

    public Mag getItem(int position){
        return mMags.get(position);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener<T>  {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}
