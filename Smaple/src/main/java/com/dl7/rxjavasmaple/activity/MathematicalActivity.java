package com.dl7.rxjavasmaple.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.dl7.androidlib.activity.BaseActivity;
import com.dl7.rxjavasmaple.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func2;

public class MathematicalActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_content)
    TextView mTvContent;
    @Bind(R.id.btn_show)
    Button mBtnShow;

    public Subscriber<Integer> mIntSub = new Subscriber<Integer>() {
        @Override
        public void onCompleted() {
            mTvContent.append("onCompleted\n");
        }

        @Override
        public void onError(Throwable e) {
            mTvContent.append("onError: " + e.toString());
        }

        @Override
        public void onNext(Integer integer) {
            mTvContent.append("onNext: " + integer + "\n");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mathematical);
        ButterKnife.bind(this);
        initToolBar(mToolbar, true, "Mathematical 算术和聚合操作");
    }

    @OnClick(R.id.btn_show)
    public void onClick() {
//        doCount();
//        doConcat();
//        doReduce();
        doCollect();
    }

    private void doCollect() {
        mTvContent.setText("Collect\n");
        Observable.just(1, 2, 3, 4)
                .collect(new Func0<List<Integer>>() {
                    @Override
                    public List<Integer> call() {
                        return new ArrayList<Integer>();
                    }
                }, new Action2<List<Integer>, Integer>() {
                    @Override
                    public void call(List<Integer> integers, Integer integer) {
                        integers.add(integer);
                    }
                }).subscribe(new Subscriber<List<Integer>>() {
            @Override
            public void onCompleted() {
                mTvContent.append("onCompleted\n");
            }

            @Override
            public void onError(Throwable e) {
                mTvContent.append("onError: " + e.toString());
            }

            @Override
            public void onNext(List<Integer> list) {
                for (Integer integer : list) {
                    mTvContent.append("list: " + integer + "\n");
                }
            }
        });
    }

    private void doReduce() {
        mTvContent.setText("Reduce\n");
        Observable.just(1, 2, 3, 4, 5)
                .reduce(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer, Integer integer2) {
                        return integer + integer2;
                    }
                })
                .subscribe(mIntSub);
    }

    private void doConcat() {
        mTvContent.setText("Concat\n");
        Observable.just(1, 2, 3).delay(1, TimeUnit.SECONDS)
                .concatWith(Observable.just(11, 22, 33))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mIntSub);

    }

    private void doCount() {
        mTvContent.setText("Count\n");
        Observable.just(1, 2, 33, 44, 55)
                .count()
                .subscribe(mIntSub);
    }
}
