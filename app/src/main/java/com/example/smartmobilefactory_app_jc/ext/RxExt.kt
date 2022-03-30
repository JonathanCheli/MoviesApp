package com.example.smartmobilefactory_app_jc.ext

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject

inline fun <reified T> Subject<T>.valueOrError(): Single<T> {
    if (this is BehaviorSubject<T>) {
        val lastValue = this.value ?: return Single.error(Exception("No last value"))
        return Single.just(lastValue)
    } else {
        return Single.error(Exception("Not BehaviorSubject"))
    }
}