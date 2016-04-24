package com.dl7.androidlib.helper;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dl7.androidlib.ui.DividerItemDecoration;

/**
 * Created by long on 2016/3/30.
 */
public class RecyclerViewHelper {
    /**
     * 配置RecyclerView
     * @param view
     */
    public static void init(Context context, RecyclerView view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        view.setHasFixedSize(true);
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 配置RecyclerView
     * @param view
     */
    public static void initWithDecoration(Context context, RecyclerView view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        view.setHasFixedSize(true);
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
    }
}
