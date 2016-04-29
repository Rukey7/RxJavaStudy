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

public class ErrorHandlingActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_content)
    TextView mTvContent;
    @Bind(R.id.btn_show)
    Button mBtnShow;

    public Subscriber<Integer> mIntSub = new Subscriber<Integer>() {
        @Override
        public void onCompleted() {
            mTvContent.append("onCompleted");
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
        setContentView(R.layout.activity_error_handling);
        ButterKnife.bind(this);
        initToolBar(mToolbar, true, "ErrorHandling");
    }

    @OnClick(R.id.btn_show)
    public void onClick() {
//        doCatch();
        doRetry();
    }

    private void doRetry() {
        mTvContent.setText("Retry\n");
        Observable<Integer> ob = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onError(new Throwable("Error"));
            }
        });
        //1
//        ob.retry(3).subscribe(mIntSub);
        //2
//        ob.retry(new Func2<Integer, Throwable, Boolean>() {
//            @Override
//            public Boolean call(Integer integer, Throwable throwable) {
//                // integer为重试次数
//                return integer < 2;
//            }
//        }).subscribe(mIntSub);
        //3
        ob.retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Throwable> observable) {
                return observable.zipWith(Observable.range(1, 3), new Func2<Throwable, Integer, Integer>() {
                    @Override
                    public Integer call(Throwable throwable, Integer integer) {
                        return integer;
                    }
                }).flatMap(new Func1<Integer, Observable<?>>() {
                    @Override
                    public Observable<?> call(Integer integer) {
                        mTvContent.append("delay retry by " + integer + " second(s)\n");
                        return Observable.timer(1, TimeUnit.SECONDS);
                    }
                });
            }
        }, AndroidSchedulers.mainThread()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(mIntSub);
    }

    private void doCatch() {
        mTvContent.setText("Catch\n");
        Observable<Integer> ob = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onError(new Throwable("Error"));
//                subscriber.onError(new Exception("Exception"));
            }
        });
        //1
//        ob.onErrorReturn(new Func1<Throwable, Integer>() {
//            @Override
//            public Integer call(Throwable throwable) {
//                return 10;
//            }
//        }).subscribe(mIntSub);
        //2
//        ob.onErrorResumeNext(Observable.just(11, 22, 33)).subscribe(mIntSub);
        //3
        ob.onExceptionResumeNext(Observable.just(11, 22, 33)).subscribe(mIntSub);
    }
}
