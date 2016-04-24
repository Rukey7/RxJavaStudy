package com.dl7.rxjavastudy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dl7.androidlib.adapter.BaseRecyclerAdapter;
import com.dl7.rxjavastudy.R;
import com.dl7.rxjavastudy.entity.HomeItem;

import java.util.List;

/**
 * Created by 95 on 2016/4/21.
 */
public class HomeAdapter extends BaseRecyclerAdapter<HomeItem> {

    public HomeAdapter(Context context) {
        super(context);
    }

    public HomeAdapter(Context context, List<HomeItem> datas) {
        super(context, datas);
    }

    public HomeAdapter(Context context, HomeItem[] datas) {
        super(context, datas);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_home, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvTitle.setText(mDatas.get(position).getTitle());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, mDatas.get(position).getActivity()));
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
