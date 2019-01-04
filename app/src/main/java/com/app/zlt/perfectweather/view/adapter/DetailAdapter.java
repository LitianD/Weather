package com.app.zlt.perfectweather.view.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.zlt.perfectweather.R;
import com.app.zlt.perfectweather.base.BaseViewHolder;
import com.app.zlt.perfectweather.model.database.DetailORM;
import java.util.List;
import butterknife.BindView;


public class DetailAdapter extends BaseRecyclerViewAdapter<DetailAdapter.ViewHolder>{

    private List<DetailORM> detailORM;
    public DetailAdapter(List<DetailORM> detailORMList){
        this.detailORM=detailORMList;

    }
    @Override
    public DetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail,parent,false);
        return new ViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(DetailAdapter.ViewHolder holder,int position){

        holder.detailIconView.setImageResource(detailORM.get(position).getIconRes());
        holder.detailKey.setText(detailORM.get(position).getKey());
        holder.detailTextView.setText(detailORM.get(position).getValue());

    }

    @Override
    public int getItemCount(){
        return detailORM.size();
    }


    static class ViewHolder extends BaseViewHolder{
        @BindView(R.id.detail_icon)
        ImageView detailIconView;

        @BindView(R.id.detail_txt)
        TextView detailTextView;

        @BindView(R.id.detail_key)
        TextView detailKey;


        ViewHolder(View itemView, final DetailAdapter adapter){
            super(itemView);
        }
    }

}
