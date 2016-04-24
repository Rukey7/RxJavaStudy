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
import rx.functions.Action1;
import rx.functions.Func0;

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
        odBuffer();
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
                odBuffer();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void odBuffer() {
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
