package com.rxjava.demo

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * 通过提供 add 的方式，进行 统一管理
 *
 * created on 2020-07-22 -- 21:06
 * @author yline
 */
abstract class BaseDisposableComponent {
    private val mCompositeDisposable = CompositeDisposable()

    fun Disposable.addToComposite() =
            apply { mCompositeDisposable.add(this) }

    fun clearDisposable() {
        mCompositeDisposable.clear()
    }
}