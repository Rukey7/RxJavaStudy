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
import rx.observables.ConnectableObservable;

public class ConnectActivity extends BaseActivity {

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
        setContentView(R.layout.activity_connect);
        ButterKnife.bind(this);
        initToolBar(mToolbar, true, "Connect 连接操作");
    }

    @OnClick(R.id.btn_show)
    public void onClick() {
//        doConnect();
//        doPublish();
//        doShare();
        doReplay();
    }

    private void doReplay() {
        mTvContent.setText("Replay\n");
        ConnectableObservable<Long> cob = Observable.interval(500, TimeUnit.MILLISECONDS)
                .take(5).observeOn(AndroidSchedulers.mainThread()).replay();
        cob.subscribe(mLongSub);
        cob.delaySubscription(1100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())  // 第二个观察者会在delay指定线程里运行
                .subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                mTvContent.append("Second onCompleted\n");
            }

            @Override
            public void onError(Throwable e) {
                mTvContent.append("Second onError: " + e.toString());
            }

            @Override
            public void onNext(Long integer) {
                mTvContent.append("Second onNext: " + integer + "\n");
            }
        });
        cob.connect();
    }

    private void doShare() {
        mTvContent.setText("Share\n");
        Observable<Long> cob = Observable.interval(500, TimeUnit.MILLISECONDS)
                .take(5).observeOn(AndroidSchedulers.mainThread()).share();
        cob.subscribe(mLongSub);
        cob.delaySubscription(1100, TimeUnit.MILLISECONDS).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                mTvContent.append("Second onCompleted\n");
            }

            @Override
            public void onError(Throwable e) {
                mTvContent.append("Second onError: " + e.toString());
            }

            @Override
            public void onNext(Long integer) {
                mTvContent.append("Second onNext: " + integer + "\n");
            }
        });
    }

    private void doPublish() {
        mTvContent.setText("Publish\n");
        ConnectableObservable<Long> cob = Observable.interval(500, TimeUnit.MILLISECONDS)
                .take(5).observeOn(AndroidSchedulers.mainThread()).publish(new Func1<Observable<Long>, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(Observable<Long> longObservable) {
                        return longObservable.startWith(11L, 12L);
                    }
                }).publish();
        cob.subscribe(mLongSub);
        cob.delaySubscription(1100, TimeUnit.MILLISECONDS).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                mTvContent.append("Second onCompleted\n");
            }

            @Override
            public void onError(Throwable e) {
                mTvContent.append("Second onError: " + e.toString());
            }

            @Override
            public void onNext(Long integer) {
                mTvContent.append("Second onNext: " + integer + "\n");
            }
        });
        cob.connect();
    }

    private void doConnect() {
        mTvContent.setText("Connect\n");
        ConnectableObservable<Long> cob = Observable.interval(500, TimeUnit.MILLISECONDS)
                .take(5).observeOn(AndroidSchedulers.mainThread()).publish();
        cob.subscribe(mLongSub);
        cob.delaySubscription(1100, TimeUnit.MILLISECONDS).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                mTvContent.append("Second onCompleted\n");
            }

            @Override
            public void onError(Throwable e) {
                mTvContent.append("Second onError: " + e.toString());
            }

            @Override
            public void onNext(Long integer) {
                mTvContent.append("Second onNext: " + integer + "\n");
            }
        });
        cob.connect();
    }
}
