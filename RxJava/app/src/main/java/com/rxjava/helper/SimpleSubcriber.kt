package com.rxjava.helper

import io.reactivex.subscribers.DisposableSubscriber

abstract class SimpleSubscriber<T> : DisposableSubscriber<T>() {
    override fun onComplete() {
    }

    override fun onError(e: Throwable) {
        onResult(false, null)
    }

    override fun onNext(t: T) {
        onResult(true, t)
    }

    protected abstract fun onResult(success: Boolean, t: T?)
}