package com.rxjava;

import android.os.Bundle;
import android.view.View;

import com.yline.log.LogFileUtil;
import com.yline.test.BaseTestActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 借鉴：https://juejin.im/user/573dba2171cfe448aa97b7b0
 * @author yline 2017/10/25 -- 20:55
 * @version 1.0.0
 */
public class MainActivity extends BaseTestActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("testSubscribe", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testSubscribe();
            }
        });

        addButton("testCreate", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testComplete();
            }
        });

        addButton("testMap", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testMap();
            }
        });

        addButton("testFlatMap", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testFlatMap();
            }
        });

        addButton("testConcatMap", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testConcatMap();
            }
        });

        addButton("testZip", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testZip();
            }
        });
    }

    /**
     * 最简单的案例
     */
    private void testSubscribe() {
        // 创建一个上游 Observable：
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        });
        // 创建一个下游 Observer
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogFileUtil.v("subscribe");
            }

            @Override
            public void onNext(Integer value) {
                LogFileUtil.v("" + value);
            }

            @Override
            public void onError(Throwable e) {
                LogFileUtil.v("error");
            }

            @Override
            public void onComplete() {
                LogFileUtil.v("complete");
            }
        };

        //建立连接
        observable.subscribe(observer);
    }

    /**
     * 截断被调用，的执行
     */
    private void testComplete() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                LogFileUtil.v("emit 1");
                emitter.onNext(1);

                LogFileUtil.v("emit 2");
                emitter.onNext(2);

                LogFileUtil.v("emit 3");
                emitter.onNext(3);

                LogFileUtil.v("emit complete");
                emitter.onComplete();

                LogFileUtil.v("emit 4");
                emitter.onNext(4);
            }
        }).subscribe(new Observer<Integer>() {
            private Disposable mDisposable;
            private int i;

            @Override
            public void onSubscribe(Disposable d) {
                LogFileUtil.v("subscribe");
                mDisposable = d;
            }

            @Override
            public void onNext(Integer value) {
                LogFileUtil.v("onNext: " + value);
                i++;
                if (i == 2) {
                    LogFileUtil.v("dispose");
                    mDisposable.dispose();
                    LogFileUtil.v("isDisposed : " + mDisposable.isDisposed());
                }
            }

            @Override
            public void onError(Throwable e) {
                LogFileUtil.v("error");
            }

            @Override
            public void onComplete() {
                LogFileUtil.v("complete");
            }
        });
    }

    /**
     * 默认有序执行
     */
    private void testMap() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "This is result " + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogFileUtil.v(s);
            }
        });
    }

    /**
     * 延迟了就变成，无序执行，
     */
    private void testFlatMap() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogFileUtil.v(s);
            }
        });
    }

    /**
     * 延迟了就 依旧，有序执行
     */
    private void testConcatMap() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                return Observable.fromIterable(list).delay(10, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogFileUtil.v(s);
            }
        });
    }

    /**
     * 多个调用者都调用之后，才执行，被调用者
     */
    private void testZip() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                try {
                    LogFileUtil.v("emit 1");
                    emitter.onNext(1);
                    Thread.sleep(200);

                    LogFileUtil.v("emit 2");
                    emitter.onNext(2);
                    Thread.sleep(200);

                    LogFileUtil.v("emit 3");
                    emitter.onNext(3);
                    Thread.sleep(200);

                    LogFileUtil.v("emit 4");
                    emitter.onNext(4);
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    LogFileUtil.e("","",e);
                }
            }
        }).subscribeOn(Schedulers.io());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                try {
                    LogFileUtil.v("emit A");
                    emitter.onNext("A");
                    Thread.sleep(200);

                    LogFileUtil.v("emit B");
                    emitter.onNext("B");
                    Thread.sleep(200);

                    LogFileUtil.v("emit C");
                    emitter.onNext("C");
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    LogFileUtil.e("","",e);
                }
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogFileUtil.v("onSubscribe");
            }

            @Override
            public void onNext(String value) {
                LogFileUtil.v("onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {
                LogFileUtil.v("onError");
            }

            @Override
            public void onComplete() {
                LogFileUtil.v("onComplete");
            }
        });
    }
}
