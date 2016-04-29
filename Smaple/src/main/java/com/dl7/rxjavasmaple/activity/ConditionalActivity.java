package com.dl7.rxjavasmaple.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.dl7.androidlib.activity.BaseActivity;
import com.dl7.rxjavasmaple.R;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;

public class ConditionalActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_content)
    TextView mTvContent;
    @Bind(R.id.btn_show)
    Button mBtnShow;

    public Subscriber<Boolean> mBooleanSub = new Subscriber<Boolean>() {
        @Override
        public void onCompleted() {
            mTvContent.append("onCompleted\n");
        }

        @Override
        public void onError(Throwable e) {
            mTvContent.append("onError: " + e.toString());
        }

        @Override
        public void onNext(Boolean integer) {
            mTvContent.append("onNext: " + integer + "\n");
        }
    };
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
        setContentView(R.layout.activity_conditional);
        ButterKnife.bind(this);
        initToolBar(mToolbar, true, "Conditional 条件和布尔操作");
    }

    @OnClick(R.id.btn_show)
    public void onClick() {
//        doAll();
//        doAmb();
//        doContains();
//        doDefaultIfEmpty();
//        doSequenceEqual();
//        doSkipUntil();
//        doSkipWhile();
//        doTakeUntil();
        doTakeWhile();
    }

    private void doTakeWhile() {
        mTvContent.setText("TakeWhile\n");
        Observable.just(1, 2, 3, 4, 5, 6)
                .takeWhile(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer < 4;
                    }
                })
                .subscribe(mIntSub);
    }

    private void doTakeUntil() {
        mTvContent.setText("TakeUntil\n");
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .take(8)
                .takeUntil(Observable.just(11, 22, 33).delay(1000, TimeUnit.MILLISECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        mTvContent.append("onCompleted\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mTvContent.append("onError: " + e.toString());
                    }

                    @Override
                    public void onNext(Long integer) {
                        mTvContent.append("onNext: " + integer + "\n");
                    }
                });
    }

    private void doSkipWhile() {
        mTvContent.setText("SkipWhile\n");
        Observable.just(1, 2, 3, 4, 5, 6)
                .skipWhile(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer < 4;
                    }
                })
                .subscribe(mIntSub);
    }

    private void doSkipUntil() {
        mTvContent.setText("SkipUntil\n");
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .take(8)
                .skipUntil(Observable.just(11, 22, 33).delay(1000, TimeUnit.MILLISECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        mTvContent.append("onCompleted\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mTvContent.append("onError: " + e.toString());
                    }

                    @Override
                    public void onNext(Long integer) {
                        mTvContent.append("onNext: " + integer + "\n");
                    }
                });
    }

    private void doSequenceEqual() {
        mTvContent.setText("SequenceEqual\n");
        Observable.sequenceEqual(Observable.just(1, 2, 3), Observable.just(1, 3, 2), new Func2<Integer, Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer, Integer integer2) {
                return integer == integer2;
            }
        }).subscribe(mBooleanSub);
    }

    private void doDefaultIfEmpty() {
        mTvContent.setText("defaultIfEmpty\n");
        Observable.empty()
                .defaultIfEmpty(5)
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        mTvContent.append("onCompleted\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mTvContent.append("onError: " + e.toString());
                    }

                    @Override
                    public void onNext(Object integer) {
                        mTvContent.append("onNext: " + integer + "\n");
                    }
                });
    }

    private void doContains() {
        mTvContent.setText("Contains\n");
        Observable.just(1, 23, 45, 214)
                .contains(45)
                .subscribe(mBooleanSub);
    }

    private void doAmb() {
        mTvContent.setText("Amb\n");
        Observable.just(1, 2, 3).delay(1000, TimeUnit.MILLISECONDS)
                .ambWith(Observable.just(11, 22, 33).delay(500, TimeUnit.MILLISECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mIntSub);
    }

    private void doAll() {
        mTvContent.setText("All\n");
        Observable.just(1, 2, 3, 5)
                .all(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer < 5;
                    }
                })
                .subscribe(mBooleanSub);
    }
}
