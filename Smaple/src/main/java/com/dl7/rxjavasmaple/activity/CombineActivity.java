package com.dl7.rxjavasmaple.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class CombineActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_content)
    TextView mTvContent;
    @Bind(R.id.btn_show)
    Button mBtnShow;

    private Observable<Integer> mIntegerObservable;
    private Observable<Long> mLongObservable;
    private Observable<String> mStringObservable;
    private Observable<String> mColorOb;
    private Observable<String> mShapeOb;

    private Subscriber<String> mStrSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine);
        ButterKnife.bind(this);
        initToolBar(mToolbar, true, "Combining");

        initVariables();
    }

    private void initVariables() {
//        mIntegerObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
//            @Override
//            public void call(Subscriber<? super Integer> subscriber) {
//                for (int i = 0; i < 5; i++) {
//                    subscriber.onNext(i*10);
//                    try {
//                        if (i == 0 || i == 2) {
//                            Thread.sleep(500);
//                        } else if (i == 1 || i == 3) {
//                            Thread.sleep(1000);
//                        }
//                    } catch (InterruptedException e) {
//                        subscriber.onError(e);
//                    }
//                    subscriber.onCompleted();
//                }
//            }
//        }).subscribeOn(Schedulers.computation());
//        mLongObservable = Observable.interval(800, TimeUnit.MILLISECONDS).take(4);
        mIntegerObservable = Observable.just(10, 20, 30, 40, 50);
        mLongObservable = Observable.just(1L, 2L, 3L, 4L);
        mStringObservable = Observable.just("hello", "world", "rxjava");
        mStrSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mTvContent.append("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                mTvContent.append("onError: " + e.toString());
            }

            @Override
            public void onNext(String integer) {
                mTvContent.append("onNext: " + integer + "\n");
            }
        };

        mColorOb = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    for (int i = 0; i < 4; i++) {
                        if (i == 0) {
                            subscriber.onNext("紫色");
                            Thread.sleep(1000);
                        } else if (i == 1) {
                            subscriber.onNext("褐色");
                            Thread.sleep(500);
                        } else if (i == 2) {
                            subscriber.onNext("青色");
                            Thread.sleep(1000);
                        } else if (i == 3) {
                            subscriber.onNext("黄色");
                            Thread.sleep(1000);
                        }
                    }
                    subscriber.onCompleted();
                } catch (InterruptedException e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread());
        mShapeOb = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    for (int i = 0; i < 3; i++) {
                        if (i == 0) {
                            Thread.sleep(500);
                            subscriber.onNext("◇");
                            Thread.sleep(1400);
                        } else if (i == 1) {
                            subscriber.onNext("☆");
                            Thread.sleep(1100);
                        } else if (i == 2) {
                            subscriber.onNext("△");
                            Thread.sleep(500);
                        }
                    }
                    subscriber.onCompleted();
                } catch (InterruptedException e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread());
    }

    @OnClick(R.id.btn_show)
    public void onClick() {
//        doZip();
        doJoin();
//        doMegre();
//        doStartWith();
//        doCombineLatest();
//        doSwitchOnNext();
    }

    private void doSwitchOnNext() {
        mTvContent.setText("SwitchOnNext\n");
        Observable.switchOnNext(Observable.create(new Observable.OnSubscribe<Observable<String>>() {
            @Override
            public void call(Subscriber<? super Observable<String>> subscriber) {
                subscriber.onNext(mColorOb);
                try {
                    Thread.sleep(1100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(mShapeOb);
                subscriber.onCompleted();
            }
        })).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(mStrSubscriber);
    }

    private void doCombineLatest() {
        mTvContent.setText("CombineLatest\n");
        //1
//        Observable.combineLatest(mColorOb, mShapeOb, new Func2<String, String, String>() {
//            @Override
//            public String call(String s, String s2) {
//                return s+s2;
//            }
//        }).observeOn(AndroidSchedulers.mainThread()).subscribe(mStrSubscriber);
        //2
        mColorOb.withLatestFrom(mShapeOb, new Func2<String, String, String>() {
            @Override
            public String call(String s, String s2) {
                return s+s2;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(mStrSubscriber);
    }

    private void doStartWith() {
        mTvContent.setText("StartWith\n");
        //1
        mIntegerObservable.startWith(100, 200)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        mTvContent.append("onNext: " + integer + "\n");
                    }
                });
        //2

    }

    private void doMegre() {
        mTvContent.setText("Megre\n");
        //1
        Observable.merge(mColorOb, mShapeOb)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mStrSubscriber);
    }

    private void doJoin() {
        mTvContent.setText("Join\n");
        //1
//        mColorOb.join(mShapeOb, new Func1<String, Observable<Object>>() {
//            @Override
//            public Observable<Object> call(String s) {
//                return Observable.empty().delay(700, TimeUnit.MILLISECONDS);
//            }
//        }, new Func1<String, Observable<Object>>() {
//            @Override
//            public Observable<Object> call(String s) {
//                return Observable.empty().delay(700, TimeUnit.MILLISECONDS);
//            }
//        }, new Func2<String, String, String>() {
//            @Override
//            public String call(String s, String s2) {
//                return s+s2;
//            }
//        }).observeOn(AndroidSchedulers.mainThread()).subscribe(mStrSubscriber);
        //2
        mColorOb.groupJoin(mShapeOb, new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                return Observable.just(s).delay(700, TimeUnit.MILLISECONDS);
            }
        }, new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                return Observable.just(s).delay(700, TimeUnit.MILLISECONDS);
            }
        }, new Func2<String, Observable<String>, Observable<String>>() {
            @Override
            public Observable<String> call(final String s, Observable<String> stringObservable) {
                // stringObservable会被消费掉???
                return stringObservable.map(new Func1<String, String>() {
                    @Override
                    public String call(String s2) {
                        Log.w("ATAG", s2);
                        return s + s2;
                    }
                });
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Observable<String>>() {
            @Override
            public void onCompleted() {
                mTvContent.append("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                mTvContent.append("onError: " + e.toString());
            }

            @Override
            public void onNext(Observable<String> stringObservable) {
                stringObservable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                mTvContent.append("onNext: " + s + "\n");
                            }
                        });
            }
        });
    }

    private void doZip() {
        mTvContent.setText("Zip\n");
        //1
//        Observable.zip(mIntegerObservable, mLongObservable, mStringObservable, new Func3<Integer, Long, String, String>() {
//            @Override
//            public String call(Integer integer, Long aLong, String s) {
//                return "int=" + integer + "; " + "long=" + aLong + "; " + "String=" + s;
//            }
//        }).subscribe(mStrSubscriber);
        //2
        mIntegerObservable.zipWith(mLongObservable, new Func2<Integer, Long, String>() {
            @Override
            public String call(Integer integer, Long s) {
                return integer + " - " + s;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(mStrSubscriber);
    }
}
