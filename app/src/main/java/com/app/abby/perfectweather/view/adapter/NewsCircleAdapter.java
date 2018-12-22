package com.app.abby.perfectweather.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.abby.perfectweather.R;
import com.app.abby.perfectweather.model.data.NewsBean;

import java.util.List;

public class NewsCircleAdapter extends RecyclerView.Adapter<NewsCircleAdapter.ViewHolder> {
    private Context mContext;

    private List<NewsBean.Bean> mDatas;
    public NewsCircleAdapter(List<NewsBean.Bean> data) {
        this.mDatas = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.circle_of_friends,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position) {

        holder.image2.setVisibility(View.GONE);
        holder.image3.setVisibility(View.GONE);
        holder.textView.setText(mDatas.get(position).getLtitle());
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView image1;
        ImageView image2;
        ImageView image3;
        TextView textView;
        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            image1 = (ImageView) view.findViewById(R.id.friend_image1);
            image2 = (ImageView) view.findViewById(R.id.friend_image1);
            image3 = (ImageView) view.findViewById(R.id.friend_image1);
            textView = (TextView) view.findViewById(R.id.friend_name);
        }
    }


}
