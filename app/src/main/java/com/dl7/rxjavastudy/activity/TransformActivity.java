package com.dl7.rxjavastudy.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dl7.androidlib.activity.BaseActivity;
import com.dl7.rxjavastudy.R;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;

public class TransformActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.sample_output)
    TextView mSampleOutput;
    @Bind(R.id.sample_result)
    TextView mSampleResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transform);
        ButterKnife.bind(this);

        initToolBar(mToolbar, true, "Transforming 变换操作");
        doBuffer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.transform, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.buffer:
                doBuffer();
                break;
            case R.id.map:
                doMap();
                break;
            case R.id.flat_map:
                doFlatMap();
                break;
            case R.id.group_by:
                doGroupBy();
                break;
            case R.id.scan:
                doScan();
                break;
            case R.id.window:
                doWindow();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void doWindow() {
        mToolbar.setSubtitle("Window");
        mSampleOutput.setText("这个window的变体立即打开它的第一个窗口。每当当前窗口发射了count项数据，" +
                "它就关闭当前窗口并打开一个新窗口。如果从原始Observable收到了onError或onCompleted通知它也会关闭当前窗口。" +
                "这种window变体发射一系列不重叠的窗口，这些窗口的数据集合与原始Observable发射的数据是一一对应的。");
        mSampleResult.setText("");
        Observable.range(1, 10)
                .window(2, 3)
                .subscribe(new Action1<Observable<Integer>>() {
                    @Override
                    public void call(Observable<Integer> result) {
                        result.subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                mSampleResult.append("onNext: " + integer + "\n");
                            }
                        });
                    }
                });
    }

    private void doScan() {
        mToolbar.setSubtitle("GroupBy");
        mSampleOutput.setText("Scan操作符对原始Observable发射的第一项数据应用一个函数，然后将那个函数的结果作为自己的第一项数据发射。" +
                "它将函数的结果同第二项数据一起填充给这个函数来产生它自己的第二项数据。它持续进行这个过程来产生剩余的数据序列。" +
                "这个操作符在某些情况下被叫做accumulator。");
        mSampleResult.setText("");
        Observable.range(1, 6)
                .scan(10, new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer i, Integer j) {
                        return i + j;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        mSampleResult.append("onNext: " + integer + "\n");
                    }
                });
    }

    private void doGroupBy() {
        mToolbar.setSubtitle("GroupBy");
        mSampleOutput.setText("GroupBy操作符将原始Observable分拆为一些Observables集合，" +
                "它们中的每一个发射原始Observable数据序列的一个子序列。哪个数据项由哪一个Observable发射是由一个函数判定的，" +
                "这个函数给每一项指定一个Key，Key相同的数据会被同一个Observable发射。\n" +
                "RxJava实现了groupBy操作符。它返回Observable的一个特殊子类GroupedObservable，" +
                "实现了GroupedObservable接口的对象有一个额外的方法getKey，这个Key用于将数据分组到指定的Observable。\n" +
                "有一个版本的groupBy允许你传递一个变换函数，这样它可以在发射结果GroupedObservable之前改变数据项。");
        mSampleResult.setText("");
        Observable.range(1, 10)
                .groupBy(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        return integer % 3;
                    }
                })
                .subscribe(new Action1<GroupedObservable<Integer, Integer>>() {
                    @Override
                    public void call(final GroupedObservable<Integer, Integer> result) {
                        result.subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                mSampleResult.append("key: " + result.getKey() + "; value: " + integer + "\n");
                            }
                        });
                    }
                });
    }

    private void doFlatMap() {
        mToolbar.setSubtitle("FlatMap");
        mSampleOutput.setText("FlatMap操作符使用一个指定的函数对原始Observable发射的每一项数据执行变换操作，" +
                "这个函数返回一个本身也发射数据的Observable，然后FlatMap合并这些Observables发射的数据，" +
                "最后将合并后的结果当做它自己的数据序列发射。\n" +
                "这个方法是很有用的，例如，当你有一个这样的Observable：它发射一个数据序列，" +
                "这些数据本身包含Observable成员或者可以变换为Observable，" +
                "因此你可以创建一个新的Observable发射这些次级Observable发射的数据的完整集合。\n" +
                "注意：FlatMap对这些Observables发射的数据做的是合并(merge)操作，因此它们可能是交错的。");
        mSampleResult.setText("");
        Observable.just(1, 11, 21).flatMap(new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                int delay = 200;
                if (integer > 10) {
                    delay = 180;
                }
                return Observable.range(integer, 5)
                        .delay(delay, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread());
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                mSampleResult.append("onNext: " + integer + "\n");
            }
        });

    }

    private void doMap() {
        mToolbar.setSubtitle("Map");
        mSampleOutput.setText("Map操作符对原始Observable发射的每一项数据应用一个你选择的函数，然后返回一个发射这些结果的Observable。\n" +
                "RxJava将这个操作符实现为map函数。这个操作符默认不在任何特定的调度器上执行。\n");
        mSampleResult.setText("");
        Observable.just(1, 2, 3)
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        return integer + 100;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        mSampleResult.append("onNext: " + integer + "\n");
                    }
                });
    }

    private void doBuffer() {
        mToolbar.setSubtitle("Buffer");
        mSampleOutput.setText("定期收集Observable的数据放进一个数据包裹，然后发射这些数据包裹，而不是一次发射一个值。\n" +
                "buffer(count)以列表(List)的形式发射非重叠的缓存，每一个缓存至多包含来自原始Observable的count项数据" +
                "（最后发射的列表数据可能少于count项）\n" +
                "buffer(count, skip)从原始Observable的第一项数据开始创建新的缓存，此后每当收到skip项数据，" +
                "用count项数据填充缓存：开头的一项和后续的count-1项，它以列表(List)的形式发射缓存，取决于count和skip的值，" +
                "这些缓存可能会有重叠部分（比如skip < count时），也可能会有间隙（比如skip > count时）。\n");
        mSampleResult.setText("");
//        Observable.range(100, 16)
//                .buffer(3, 5)
//                .subscribe(new Action1<List<Integer>>() {
//                    @Override
//                    public void call(List<Integer> integers) {
//                        mSampleResult.append("onNext: " + Arrays.toString(integers.toArray()) + "\n");
//                    }
//                });
        Observable.range(1, 14)
                .buffer(new Func0<Observable<?>>() {
                    @Override
                    public Observable<?> call() {
                        return Observable.never().delay(2, TimeUnit.SECONDS);
//                        return Observable.create(new Observable.OnSubscribe<Integer>() {
//                            @Override
//                            public void call(Subscriber<? super Integer> subscriber) {
////                                subscriber.onNext(123);
//                            }
//                        });
                    }
                })
                .subscribe(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> integers) {
                        mSampleResult.append("onNext: " + Arrays.toString(integers.toArray()) + "\n");
                    }
                });
    }
}
