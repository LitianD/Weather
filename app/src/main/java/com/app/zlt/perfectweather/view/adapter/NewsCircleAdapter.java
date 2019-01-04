package com.app.zlt.perfectweather.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.zlt.perfectweather.activity.ADetailActivity;
import com.app.zlt.perfectweather.util.Resolution;
import com.bumptech.glide.Glide;

import com.app.zlt.perfectweather.R;
import com.app.zlt.perfectweather.model.data.NewsBean;

import java.util.List;

public class NewsCircleAdapter extends RecyclerView.Adapter<NewsCircleAdapter.ViewHolder> {
    private Context context;

    private List<NewsBean.Bean> mDatas;
    public NewsCircleAdapter(List<NewsBean.Bean> data) {
        this.mDatas = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.circle_of_friends,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position) {
        final NewsBean.Bean bean = mDatas.get(position);
        if (bean == null) {
            return;
        }
        int widthPixels=context.getResources().getDisplayMetrics().widthPixels;
        int width = widthPixels;
        int height = Resolution.dipToPx(context, 200);
        Glide.with(context)
                .load(bean.getImgsrc())
                .error(R.drawable.img_error)
                .placeholder(R.drawable.loads)
                .override(width,height)
                .centerCrop()
                .into(((ViewHolder) holder).ivNewsImg);
        if (position == 0) {
            ((ViewHolder) holder).tvNewsDigest.setVisibility(View.GONE);
            ((ViewHolder) holder).tvNewsTitle.setText("图片：" + bean.getTitle());
        } else {
            ((ViewHolder) holder).tvNewsTitle.setText(bean.getTitle());
            ((ViewHolder) holder).tvNewsDigest.setText(bean.getMtime() + " : " + bean.getDigest());
            ((ViewHolder) holder).cvNews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ADetailActivity.class);
                    intent.putExtra("url", bean.getUrl_3w());
                    intent.putExtra("title", bean.getTitle());
                    context.startActivity(intent);
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivNewsImg;
        private TextView tvNewsTitle;
        private TextView tvNewsDigest;
        private CardView cvNews;

        public ViewHolder(View view) {
            super(view);
            ivNewsImg = (ImageView) view.findViewById(R.id.iv_news_img);
            tvNewsTitle = (TextView) view.findViewById(R.id.tv_news_title);
            tvNewsDigest = (TextView) view.findViewById(R.id.tv_news_digest);
            cvNews = (CardView) view.findViewById(R.id.cv_news);
        }

    }


}
