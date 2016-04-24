package com.dl7.rxjavastudy.entity;

/**
 * Created by long on 2016/4/22.
 */
public class HomeItem {

    private String title;
    private Class activity;

    public HomeItem(String title, Class activity) {
        this.title = title;
        this.activity = activity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class getActivity() {
        return activity;
    }

    public void setActivity(Class activity) {
        this.activity = activity;
    }
}
