package com.example.jianancangku.view.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jianancangku.R;
import com.example.jianancangku.bean.Thingbookbean;

import java.util.List;

public class ThingbookAdapter extends RecyclerView.Adapter<ThingbookAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<Thingbookbean.ListBean> mDatas;


    public ThingbookAdapter(Context context, List<Thingbookbean.ListBean> datas) {
        //这里适配器是写给主活动互相调用的方法
        this.context = context;
        this.mDatas = datas;
        this.inflater = LayoutInflater.from(context);

    }


    @NonNull
    @Override
    public ThingbookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.thingscar_item_layout, parent, false);
        ThingbookAdapter.ViewHolder viewHolder = new ThingbookAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ThingbookAdapter.ViewHolder holder, int position) {
        //        if (mDatas.get(position).getType().equals("2"))
//            holder.thingnow.setText("已出库");
//        else if (mDatas.get(position).getType().equals("1"))
//            holder.thingnow.setText("已入库");
        holder.thingnow.setVisibility(View.GONE);
        holder.thing_number.setText((String
                .format(context.getString(R.string.bigitem_number)
                        , mDatas.get(position).getPackage_sn())));
        holder.gohouse_time.setText((String
                .format(context.getString(R.string.shopitem_out_timer)
                        , mDatas.get(position).getCreate_time())));
        holder.gohome_address.setText(String
                .format(context.getString(R.string.shopitem_adrress)
                        , mDatas.get(position).getBusiness_address()));

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView thing_number, gohouse_time, gohome_address,thingnow;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thingnow = itemView.findViewById(R.id.thingnow);
            thing_number = itemView.findViewById(R.id.thing_number);
            gohouse_time = itemView.findViewById(R.id.gohouse_time);
            gohome_address = itemView.findViewById(R.id.gohome_address);

        }
    }
}
