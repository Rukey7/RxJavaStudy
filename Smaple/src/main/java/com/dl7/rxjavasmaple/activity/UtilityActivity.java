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
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.TimeInterval;
import rx.schedulers.Timestamped;

public class UtilityActivity extends BaseActivity {

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
    public Subscriber<Long> mLongSub = new Subscriber<Long>() {
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility);
        ButterKnife.bind(this);
        initToolBar(mToolbar, true, "Utility 辅助操作");
    }

    @OnClick(R.id.btn_show)
    public void onClick() {
//        doDelay();
//        doDo();
//        doForEach();
//        doTimeInterval();
//        doTimeout();
//        doTimestamp();
//        doUsing();
        doMaterialize();
    }

    private void doMaterialize() {
        mTvContent.setText("Metrialize\n");
        //1
//        Observable.just(1, 2, 3)
//                .materialize()
//                .subscribe(new Subscriber<Notification<Integer>>() {
//                    @Override
//                    public void onCompleted() {
//                        mTvContent.append("onCompleted\n");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mTvContent.append("onError: " + e.toString());
//                    }
//
//                    @Override
//                    public void onNext(Notification<Integer> integerNotification) {
//                        mTvContent.append("onNext: " + integerNotification + "\n");
//                    }
//                });
        //2
        Observable.just(1, 2, 3)
                .materialize()
                .dematerialize()
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
                    public void onNext(Object o) {
                        mTvContent.append("onNext: " + o + "\n");
                    }
                });
    }

    private void doUsing() {
        mTvContent.setText("Using\n");
        Observable.using(new Func0<Integer>() {
            @Override
            public Integer call() {
                return 100;
            }
        }, new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                return Observable.range(integer, 5);
            }
        }, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                // 如果资源是对象这里释放对象
            }
        }).subscribe(mIntSub);
    }

    private void doTimestamp() {
        mTvContent.setText("Timestamp\n");
        Observable.interval(600, TimeUnit.MILLISECONDS)
                .take(5)
                .timestamp()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Timestamped<Long>>() {
                    @Override
                    public void onCompleted() {
                        mTvContent.append("onCompleted\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mTvContent.append("onError: " + e.toString());
                    }

                    @Override
                    public void onNext(Timestamped<Long> longTimestamped) {
                        mTvContent.append("onNext: " + longTimestamped + "\n");
                    }
                });
    }

    private void doTimeout() {
        mTvContent.setText("Timeout\n");
        //1
        Observable.interval(1000, TimeUnit.MILLISECONDS)
                .take(5)
                .mergeWith(Observable.interval(300, TimeUnit.MILLISECONDS).take(3))
                .timeout(700, TimeUnit.MILLISECONDS, Observable.just(11L, 12L))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mLongSub);
    }

    private void doTimeInterval() {
        mTvContent.setText("TimeInterval\n");
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .take(5)
                .timeInterval()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TimeInterval<Long>>() {
                    @Override
                    public void onCompleted() {
                        mTvContent.append("onCompleted\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mTvContent.append("onError: " + e.toString());
                    }

                    @Override
                    public void onNext(TimeInterval<Long> longTimeInterval) {
                        mTvContent.append("onNext: " + longTimeInterval + "\n");
                    }
                });
    }

    private void doForEach() {
        mTvContent.setText("ForEach\n");
        Observable.just(1, 2, 3)
                .forEach(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        mTvContent.append("onNext: " + integer + "\n");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mTvContent.append("onError: " + throwable.toString());
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mTvContent.append("onCompleted\n");
                    }
                });
    }

    private void doDo() {
        mTvContent.setText("Do\n");
        //1
//        Observable.just(1, 2, 3)
//                .doOnEach(new Action1<Notification<? super Integer>>() {
//                    @Override
//                    public void call(Notification<? super Integer> notification) {
//                        if (notification.isOnCompleted()) {
//                            throw new RuntimeException("doOnEach Exception");
//                        }
//                    }
//                }).subscribe(mIntSub);
        //2
//        Observable.just(1, 2, 3)
//                .doOnNext(new Action1<Integer>() {
//                    @Override
//                    public void call(Integer integer) {
//                        if (integer > 2) {
//                            throw new RuntimeException("doOnNext Exception");
//                        }
//                    }
//                }).subscribe(mIntSub);
        //3
//        Observable.just(1, 2, 3)
//                .doOnSubscribe(new Action0() {
//                    @Override
//                    public void call() {
//                        mTvContent.append("doOnSubscribe\n");
//                    }
//                }).subscribe(mIntSub);
        //4
        Observable.just(1, 2, 3)
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mTvContent.append("doAfterTerminate\n");
                    }
                }).subscribe(mIntSub);
    }

    private void doDelay() {
        mTvContent.setText("Delay\n");
        //1
//        Observable.interval(500, TimeUnit.MILLISECONDS)
//                .take(5)
//                .delay(1000, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(mLongSub);
        //2 网上文档是错误的，并不会过滤掉1200毫秒前发送的数据，而是延迟接收
//        Observable.interval(500, TimeUnit.MILLISECONDS)
//                .take(5)
//                .delaySubscription(1200, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(mLongSub);
        //3
        Observable.interval(500, TimeUnit.MILLISECONDS)
                .take(5)
                .delay(new Func0<Observable<Long>>() {
                    @Override
                    public Observable<Long> call() {
                        return Observable.timer(1300, TimeUnit.MILLISECONDS);
                    }
                }, new Func1<Long, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(Long aLong) {
                        return Observable.timer(500, TimeUnit.MILLISECONDS);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mLongSub);

    }
}
