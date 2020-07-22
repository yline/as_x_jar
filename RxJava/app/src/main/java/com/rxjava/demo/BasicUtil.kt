package com.rxjava.demo

import com.yline.log.LogUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * 常见用法
 *
 *
 *     https://juejin.im/post/5848d96761ff4b0058c9d3dc     // 给初学者的RxJava2.0教程(一)  Observable和Observer
 *     https://juejin.im/post/5848dd11b123db0066030123     // 给初学者的RxJava2.0教程(二)  切换 线程
 *     https://juejin.im/post/5848dd3eac502e00691385c5     // 给初学者的RxJava2.0教程(三)  map、flatMap
 *     https://juejin.im/post/584a6dd9128fe100589bf29d     // 给初学者的 RxJava2.0 教程 (四)  Zip
 *
 * created on 2020-07-22 -- 17:43
 * @author yline
 */
object BasicUtil {
    /**
     * 输出流的展示
     *
     * 输出内容:
     *
     * onSubscribe(L:36):: LogUtil ->
     * onNext(L:40):: LogUtil -> t = 1
     * onNext(L:40):: LogUtil -> t = 2
     * onNext(L:40):: LogUtil -> t = 3
     * onComplete(L:32):: LogUtil ->
     *
     */
    fun testSubscribe() {
        val observable = Observable.create(ObservableOnSubscribe<Int> { emitter ->
            emitter.onNext(1)
            emitter.onNext(2)
            emitter.onNext(3)
            emitter.onComplete()
        })

        observable.subscribe(object : Observer<Int> {
            override fun onComplete() {
                LogUtil.v("")
            }

            override fun onSubscribe(d: Disposable) {
                LogUtil.v("")
            }

            override fun onNext(t: Int) {
                LogUtil.v("t = $t")
            }

            override fun onError(e: Throwable) {
                LogUtil.e("", e)
            }
        })
    }

    /**
     * 输出结果
     *
     * LogUtil -> it = 1
     * LogUtil -> it = 2
     */
    fun testConsumer() {
        val observable = Observable.create(ObservableOnSubscribe<Int> { emitter ->
            emitter.onNext(1)
            emitter.onNext(2)
            emitter.onComplete()
        })

        observable.subscribe(Consumer {
            LogUtil.v("it = $it")
        }).dispose()
    }

    /**
     * 输出结果
     *
     *  LogUtil -> main 1
    LogUtil -> create -> 1

    LogUtil -> newThread 23206, 1
    LogUtil -> io 23207, 1
    LogUtil -> computation 23208, 1
    LogUtil -> main 1, 1
     */
    fun testThreadSwitch() {
        /*
            Schedulers.computation(); // 计算使用，不会阻塞
            Schedulers.newThread(); // 启用新线程
            Schedulers.io(); // IO流读取
            Schedulers.shutdown(); // 关闭所有线程
            AndroidSchedulers.mainThread(); // Android主线程
        **/
        LogUtil.v("main ${Thread.currentThread().id}")

        val observable = Observable.create(ObservableOnSubscribe<Int> {
            it.onNext(1)
            LogUtil.v("create -> ${Thread.currentThread().id}")
        })
        observable
                .subscribeOn(AndroidSchedulers.mainThread())    // 默认就是创建的线程，也可以指定某一个线程
                .observeOn(Schedulers.newThread())      // 指定 onNext 在哪个线程中执行
                .doOnNext {
                    LogUtil.v("newThread ${Thread.currentThread().id}, $it")
                }
                .observeOn(Schedulers.io())
                .doOnNext {
                    LogUtil.v("io ${Thread.currentThread().id}, $it")
                }
                .observeOn(Schedulers.computation())
                .doOnNext {
                    LogUtil.v("computation ${Thread.currentThread().id}, $it")
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    LogUtil.v("main ${Thread.currentThread().id}, $it")
                }
                .subscribe()
    }


    /**
     * 输出内容
     *
     * map result = this is result 1
     * map result = this is result 2
     * map result = this is result 3
     */
    fun testMap() {
        Observable.create<Int> {
            it.onNext(1)
            it.onNext(2)
            it.onNext(3)
        }.map { integer ->
            "this is result $integer"
        }.subscribe {
            LogUtil.v("map result = $it")
        }.dispose()

    }

    /**
     * 输出内容
     *
     * result = value: 1 - 0
     * result = value: 1 - 1
     * result = value: 2 - 0
     * result = value: 2 - 1
     * result = value: 3 - 0
     * result = value: 3 - 1
     *
     * concatMap 效果 和 flatMap 相似，只是
     *
     * flatMap 结果是无序的
     * concatMap 结果是严格按照上游发送的顺序来发送的
     */
    fun testFlatMap() {
        Observable.create(ObservableOnSubscribe<Int> { emitter ->
            emitter.onNext(1)
            emitter.onNext(2)
            emitter.onNext(3)
        }).flatMap { integer ->
            val dataList = ArrayList<String>()
            for (i in 0..1) {
                dataList.add("value: $integer - $i")
            }

            return@flatMap Observable.fromIterable(dataList)
        }.subscribe {
            LogUtil.v("result = $it")
        }.dispose()
    }


    /**
     * 输出内容
     *
     * LogUtil -> onSubscribe
     *
     * LogUtil -> emit 1
     * LogUtil -> emit A
     * LogUtil -> onNext: 1A
     *
     * LogUtil -> emit 2
     * LogUtil -> emit B
     * LogUtil -> onNext: 2B
     *
     * LogUtil -> emit 3
     * LogUtil -> emit C
     * LogUtil -> onNext: 3C
     *
     * LogUtil -> emit 4
     */
    fun testZip() {
        val observable1 = Observable.create(ObservableOnSubscribe<Int> { emitter ->
            try {
                LogUtil.v("emit 1")
                emitter.onNext(1)
                Thread.sleep(200)

                LogUtil.v("emit 2")
                emitter.onNext(2)
                Thread.sleep(200)

                LogUtil.v("emit 3")
                emitter.onNext(3)
                Thread.sleep(200)

                LogUtil.v("emit 4")
                emitter.onNext(4)
                Thread.sleep(200)
            } catch (e: InterruptedException) {
                LogUtil.e("", e)
            }
        }).subscribeOn(Schedulers.io())

        val observable2 = Observable.create(ObservableOnSubscribe<String> { emitter ->
            try {
                LogUtil.v("emit A")
                emitter.onNext("A")
                Thread.sleep(200)

                LogUtil.v("emit B")
                emitter.onNext("B")
                Thread.sleep(200)

                LogUtil.v("emit C")
                emitter.onNext("C")
                Thread.sleep(200)
            } catch (e: InterruptedException) {
                LogUtil.e("", e)
            }
        }).subscribeOn(Schedulers.io())

        Observable.zip(observable1, observable2, object : BiFunction<Int, String, String> {
            override fun apply(integer: Int, s: String): String {
                return integer.toString() + s
            }
        }).subscribe(object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                LogUtil.v("onSubscribe")
            }

            override fun onNext(value: String) {
                LogUtil.v("onNext: $value")
            }

            override fun onError(e: Throwable) {
                LogUtil.v("onError")
            }

            override fun onComplete() {
                LogUtil.v("onComplete")
            }
        })
    }


}