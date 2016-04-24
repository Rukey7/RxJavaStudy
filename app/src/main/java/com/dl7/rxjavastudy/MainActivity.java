package com.dl7.rxjavastudy;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.dl7.androidlib.activity.BaseActivity;
import com.dl7.androidlib.helper.RecyclerViewHelper;
import com.dl7.rxjavastudy.activity.CreateActivity;
import com.dl7.rxjavastudy.activity.TransformActivity;
import com.dl7.rxjavastudy.adapter.HomeAdapter;
import com.dl7.rxjavastudy.entity.HomeItem;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private static String[] mTitles = new String[] {
            "Creating 创建操作", "Transforming 变换操作", "Filtering 过滤操作", "Combining 结合操作",
            "Error Handling 错误处理", "Utility 辅助操作", "Conditional 条件和布尔操作", "Mathematical 算术和聚合操作",
            "Async 异步操作", "Connect 连接操作", "Convert 转换操作", "Blocking 阻塞操作", "String 字符串操作"
    };
    private static HomeItem[] homeItems = new HomeItem[] {
            new HomeItem("Creating 创建操作", CreateActivity.class),
            new HomeItem("Transforming 变换操作", TransformActivity.class)
    };


    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.rcv_list)
    RecyclerView mRcvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Logger.init("TAG").hideThreadInfo();
        initToolBar(mToolbar, false, R.string.app_name);
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initViews() {
        RecyclerViewHelper.init(this, mRcvList);
        HomeAdapter adapter = new HomeAdapter(this, homeItems);
        mRcvList.setAdapter(adapter);
    }

}
