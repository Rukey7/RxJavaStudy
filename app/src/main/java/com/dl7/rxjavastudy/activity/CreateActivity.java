package com.dl7.rxjavastudy.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dl7.androidlib.activity.BaseActivity;
import com.dl7.rxjavastudy.R;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

public class CreateActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.sample_output)
    TextView mSampleOutput;
    @Bind(R.id.sample_result)
    TextView mSampleResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ButterKnife.bind(this);
        initToolBar(mToolbar, true, "Creating 创建操作");
        doCreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Logger.d("onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.create:
                doCreate();
                break;
            case R.id.from:
                doFrom();
                break;
            case R.id.just:
                doJust();
                break;
            case R.id.defer:
                doDefer();
                break;
            case R.id.range:
                doRange();
                break;
            case R.id.repeat:
                doRepeat();
                break;
            case R.id.interval:
                doInterval();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doInterval() {
        mToolbar.setSubtitle("Interval");
        mSampleOutput.setText("创建一个按固定时间间隔发射整数序列的Observable。\n" +
                "Interval操作符返回一个Observable，它按固定的时间间隔发射一个无限递增的整数序列.\n" +
                "RxJava将这个操作符实现为interval方法。它接受一个表示时间间隔的参数和一个表示时间单位的参数。\n");
        mSampleResult.setText("");
        Observable.interval(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        mSampleResult.append("Sequence complete." + "\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSampleResult.append("Error: " + e.getMessage() + "\n");
                    }

                    @Override
                    public void onNext(Long integer) {
                        mSampleResult.append("Interval: " + integer + "\n");
                    }
                });
    }

    private void doRepeat() {
        mToolbar.setSubtitle("Repeat");
        mSampleOutput.setText("Repeat重复地发射数据。某些实现允许你重复的发射某个数据序列，还有一些允许你限制重复的次数。" +
                "RxJava将这个操作符实现为repeat方法。它不是创建一个Observable，而是重复发射原始Observable的数据序列，" +
                "这个序列或者是无限的，或者通过repeat(n)指定重复次数。\n" +
                "repeat操作符默认在trampoline调度器上执行。有一个变体可以通过可选参数指定Scheduler。");
        mSampleResult.setText("repeat\n");
        Observable.just(1, 2, 3)
                .repeat(3)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        mSampleResult.append("Sequence complete." + "\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSampleResult.append("Error: " + e.getMessage() + "\n");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        mSampleResult.append("Next: " + integer + "\n");
                    }
                });
        mSampleResult.append("\nrepeatWhen:" + "\n");
        Observable.just(1, 2, 3)
                .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Void> observable) {
                        return Observable.never();
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Logger.w("onCompleted");
                        mSampleResult.append("Sequence complete." + "\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                        mSampleResult.append("Error: " + e.getMessage() + "\n");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Logger.i("Next: " + integer);
                        mSampleResult.append("Next: " + integer + "\n");
                    }
                });
    }

    private void doRange() {
        mToolbar.setSubtitle("Range");
        mSampleOutput.setText("Range操作符发射一个范围内的有序整数序列，你可以指定范围的起始和长度。\n" +
                "RxJava将这个操作符实现为range函数，它接受两个参数，一个是范围的起始值，" +
                "一个是范围的数据的数目。如果你将第二个参数设为0，将导致Observable不发射任何数据（如果设置为负数，会抛异常）。\n" +
                "range默认不在任何特定的调度器上执行。有一个变体可以通过可选参数指定Scheduler。");
        mSampleResult.setText("range(100, 5)\n");
        Observable.range(100, 5)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        mSampleResult.append("Sequence complete." + "\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSampleResult.append("Error: " + e.getMessage() + "\n");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        mSampleResult.append("Next: " + integer + "\n");
                    }
                });
        mSampleResult.append("\nrange(100, -1)\n");
        try {
            Observable.range(100, -1)
                    .subscribe(new Action1<Integer>() {
                        @Override
                        public void call(Integer integer) {
                        }
                    });
        } catch (Exception e) {
            mSampleResult.append("Error: " + e.getMessage() + "\n");
        }
    }

    private void doDefer() {
        mToolbar.setSubtitle("Defer");
        mSampleOutput.setText("Defer操作符会一直等待直到有观察者订阅它，然后它使用Observable工厂方法生成一个Observable。" +
                "它对每个观察者都这样做，因此尽管每个订阅者都以为自己订阅的是同一个Observable，" +
                "事实上每个订阅者获取的是它们自己的单独的数据序列。\n" +
                "在某些情况下，等待直到最后一分钟（就是知道订阅发生时）才生成Observable可以确保Observable包含最新的数据。");
        mSampleResult.setText("");
        // 有观察者订阅时会生成一个新的Observable
        Observable<String> defer = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                String str = new String("System.currentTimeMillis(): " + System.currentTimeMillis());
                return Observable.just(str);
            }
        });
        defer.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                mSampleResult.append("Next: " + s + "\n");
            }
        });
        defer.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                mSampleResult.append("Next2: " + s + "\n");
            }
        });
    }

    private void doJust() {
        mToolbar.setSubtitle("Just");
        mSampleOutput.setText("Just将单个数据转换为发射那个数据的Observable。\n" +
                "Just类似于From，但是From会将数组或Iterable的素具取出然后逐个发射，" +
                "而Just只是简单的原样发射，将数组或Iterable当做单个数据。\n" +
                "注意：如果你传递null给Just，它会返回一个发射null值的Observable。" +
                "不要误认为它会返回一个空Observable（完全不发射任何数据的Observable），如果需要空Observable你应该使用Empty操作符。");
        mSampleResult.setText("");
        Observable.just(1, 2, 3)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onNext(Integer item) {
                        mSampleResult.append("Next: " + item + "\n");
                    }

                    @Override
                    public void onError(Throwable error) {
                        mSampleResult.append("Error: " + error.getMessage() + "\n");
                    }

                    @Override
                    public void onCompleted() {
                        mSampleResult.append("Sequence complete." + "\n");
                    }
                });
    }

    private void doFrom() {
        mToolbar.setSubtitle("From");
        mSampleOutput.setText("将其它种类的对象和数据类型转换为Observable," +
                "from操作符可以转换Future、Iterable和数组。" +
                "对于Iterable和数组，产生的Observable会发射Iterable或数组的每一项数据。" +
                "RxJava将这个操作符实现为just函数，它接受一至九个参数，返回一个按参数列表顺序发射这些数据的Observable。");
        mSampleResult.setText("");
        Integer[] items = new Integer[] {1, 2, 3, 4, 5};
        Observable.from(items)
                .subscribe(new Action1<Integer>() {
                               @Override
                               public void call(Integer integer) {
                                   mSampleResult.append("Next: " + integer + "\n");
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                mSampleResult.append("Error: " + throwable.getMessage() + "\n");
                            }
                        },
                        new Action0() {
                            @Override
                            public void call() {
                                mSampleResult.append("Sequence complete." + "\n");
                            }
                        });
    }

    private void doCreate() {
        mToolbar.setSubtitle("Create");
        mSampleOutput.setText("你可以使用Create操作符从头开始创建一个Observable，" +
                "给这个操作符传递一个接受观察者作为参数的函数，" +
                "编写这个函数让它的行为表现为一个Observable" +
                "--恰当的调用观察者的onNext，onError和onCompleted方法。");
        mSampleResult.setText("");
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        for (int i = 1; i < 5; i++) {
                            observer.onNext(i);
                        }
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        } ).subscribe(new Subscriber<Integer>() {
            @Override
            public void onNext(Integer item) {
                System.out.println("Next: " + item);
                mSampleResult.append("Next: " + item + "\n");
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("Error: " + error.getMessage());
                mSampleResult.append("Error: " + error.getMessage() + "\n");
            }

            @Override
            public void onCompleted() {
                System.out.println("Sequence complete.");
                mSampleResult.append("Sequence complete." + "\n");
            }
        });
    }
}
