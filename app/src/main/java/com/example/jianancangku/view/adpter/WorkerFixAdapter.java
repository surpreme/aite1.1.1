package com.example.jianancangku.view.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jianancangku.R;
import com.example.jianancangku.bean.ThingfixBean;
import com.example.jianancangku.bean.WorkerfixBean;

import java.util.List;

public class WorkerFixAdapter extends RecyclerView.Adapter<WorkerFixAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<WorkerfixBean.ListBean> mDatas;


    public WorkerFixAdapter(Context context, List<WorkerfixBean.ListBean> datas) {
        //这里适配器是写给主活动互相调用的方法
        this.context = context;
        this.mDatas = datas;
        this.inflater = LayoutInflater.from(context);

    }


    @NonNull
    @Override
    public WorkerFixAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.worker_fix_item_layout, parent, false);
        WorkerFixAdapter.ViewHolder viewHolder = new WorkerFixAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerFixAdapter.ViewHolder holder, int position) {
        //        if (mDatas.get(position).getType().equals("2"))
//            holder.thingnow.setText("已出库");
//        else if (mDatas.get(position).getType().equals("1"))
//            holder.thingnow.setText("已入库");
        holder.workerfix_diwei.setText(mDatas.get(position).getType().equals("1") ? R.string.gohouseworker : R.string.outhouseworker);
        holder.workerfix_name.setText(mDatas.get(position).getName());
        holder.workerfix_id.setText(mDatas.get(position).getAccount_number());

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView workerfix_name, workerfix_id, workerfix_diwei;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            workerfix_name = itemView.findViewById(R.id.workerfix_name);
            workerfix_id = itemView.findViewById(R.id.workerfix_id);
            workerfix_diwei = itemView.findViewById(R.id.workerfix_diwei);

        }
    }
}
