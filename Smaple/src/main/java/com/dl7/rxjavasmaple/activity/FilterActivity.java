package com.dl7.rxjavasmaple.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.dl7.androidlib.activity.BaseActivity;
import com.dl7.rxjavasmaple.R;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class FilterActivity extends BaseActivity {

    @Bind(R.id.tv_content)
    TextView mTvContent;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.btn_show)
    Button mBtnShow;

    private Subscriber<Integer> mIntegerSubscriber;
    private Subscriber<Long> mLongSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        initToolBar(mToolbar, true, "Filtering");

        mIntegerSubscriber = new Subscriber<Integer>() {
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
        mLongSubscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                mTvContent.append("onCompleted");
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
    }

    @OnClick(R.id.btn_show)
    public void onClick() {
//        doDebounce();
//        doDistinct();
//        doElementAt();
//        doSkip();
//        doFirst();
//        doFilter();
//        doIgnoreElement();
    }

    private void doIgnoreElement() {
        mTvContent.setText("IgnoreElement:\n");
        Observable.just(1, 2, 3)
                .ignoreElements()
                .subscribe(mIntegerSubscriber);
    }

    private void doFilter() {
        mTvContent.setText("Filter:\n");
        //1
//        Observable.just(1, 2, 3, 4, 5)
//                .filter(new Func1<Integer, Boolean>() {
//                    @Override
//                    public Boolean call(Integer integer) {
//                        return integer < 4;
//                    }
//                })
//                .subscribe(mIntegerSubscriber);
        // 2
        Observable.just(11, "hello", 12, "world", 13)
                .ofType(Integer.class)
                .subscribe(mIntegerSubscriber);
    }

    private void doFirst() {
        mTvContent.setText("First:\n");
        //1
//        Observable.just(2, 3, 4)
//                .first()
//                .subscribe(mIntegerSubscriber);
        // 2
//        Observable.interval(300, TimeUnit.MILLISECONDS)
//                .first(new Func1<Long, Boolean>() {
//                    @Override
//                    public Boolean call(Long aLong) {
//                        return aLong == 4;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(mLongSubscriber);
        // 3
//        Observable.just(1, 2, 3)
//                //.first()
//                .takeFirst(new Func1<Integer, Boolean>() {
//                    @Override
//                    public Boolean call(Integer integer) {
//                        return integer == 4;
//                    }
//                })
//                .subscribe(mIntegerSubscriber);
        // 4
        Observable.just(1, 2, 3, 4)
                .single(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer == 3;
                    }
                })
                .subscribe(mIntegerSubscriber);
    }

    private void doSkip() {
        mTvContent.setText("Skip:\n");
        //1
//        Observable.just(1, 2, 3, 4, 5)
//                .skip(2)
//                .subscribe(mIntegerSubscriber);
        //2
        Observable.interval(400, TimeUnit.MILLISECONDS)
                .take(7)
                .skip(1500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mLongSubscriber);
    }

    private void doElementAt() {
        mTvContent.setText("ElementAt:\n");
        //1
//        Observable.just(1, 2, 3)
//                .elementAt(1)
//                .subscribe(mIntegerSubscriber);
        // 2
        Observable.just(1, 2, 3)
                .elementAtOrDefault(5, 100)
                .subscribe(mIntegerSubscriber);
    }

    private void doDistinct() {
        mTvContent.setText("Distinct:\n");
        // 1
//        Observable.just(1, 2, 1, 1, 2, 3)
//                .distinct()
//                .subscribe(mIntegerSubscriber);
        // 2
//        Observable.just(1, 2, 3, 4, 5, 6, 7, 8)
//                .distinct(new Func1<Integer, Integer>() {
//                    @Override
//                    public Integer call(Integer integer) {
//                        // 返回一个KEY，过滤掉具有相同KEY的项
//                        return integer % 5;
//                    }
//                }).subscribe(mIntegerSubscriber);
        // 3
//        Observable.just(1, 2, 2, 3, 4, 7, 4, 3, 6)
//                .distinctUntilChanged()
//                .subscribe(mIntegerSubscriber);
        // 4
        Observable.just(1, 2, 2, 3, 4, 7, 4, 3, 6)
                .distinctUntilChanged(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        return integer % 3;
                    }
                })
                .subscribe(mIntegerSubscriber);
    }

    private void doDebounce() {
        mTvContent.setText("Debounce:\n");
//        Observable.create(new Observable.OnSubscribe<Integer>() {
//            @Override
//            public void call(Subscriber<? super Integer> subscriber) {
//                if (subscriber.isUnsubscribed()) {
//                    return;
//                }
//                try {
//                    for (int i = 0; i < 8; i++) {
//                        subscriber.onNext(i);
//                        Thread.sleep(100 * i);
//                    }
//                    subscriber.onCompleted();
//                } catch (InterruptedException e) {
//                    subscriber.onError(e);
//                }
//            }
//        }).subscribeOn(Schedulers.newThread())
//        .debounce(500, TimeUnit.MILLISECONDS)
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new Subscriber<Integer>() {
//            @Override
//            public void onCompleted() {
//                mTvContent.append("onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                mTvContent.append("onError: " + e.toString());
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                mTvContent.append("onNext: " + integer + "\n");
//            }
//        });

        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                try {
                    for (int i = 8; i > 0; i--) {
                        subscriber.onNext(i);
                        Thread.sleep(100 * i);
                    }
                    subscriber.onCompleted();
                } catch (InterruptedException e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread()).debounce(new Func1<Integer, Observable<Object>>() {
            @Override
            public Observable<Object> call(Integer integer) {
                // 为每一项数据生成一个Observable,每个Observable延迟550MS发送结束onCompleted(),
                // 在本例子中会抑制小于550MS间隔发送的数据
                Logger.d("debounce:" + integer);
                return Observable.empty().delay(550, TimeUnit.MILLISECONDS);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                mTvContent.append("onCompleted");
                Logger.w("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                mTvContent.append("onError: " + e.toString());
                Logger.e("onError: " + e.toString());
            }

            @Override
            public void onNext(Integer integer) {
                mTvContent.append("onNext: " + integer + "\n");
                Logger.i("onNext: " + integer);
            }
        });
    }

}
