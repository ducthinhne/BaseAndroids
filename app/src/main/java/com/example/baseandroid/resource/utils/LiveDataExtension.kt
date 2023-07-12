package com.example.baseandroid.resource.utils

import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.*
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicBoolean


fun <T> LiveData<T>.debounce(timeMillis: Long = 1000L, coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)) =
    MediatorLiveData<T>().also { mld ->

        val source = this
        var job: Job? = null

        mld.addSource(source) {
            job?.cancel()
            job = coroutineScope.launch {
                delay(timeMillis)
                mld.value = source.value
            }
        }
    }

fun <A, B> LiveData<A>.zipWith(stream: LiveData<B>): LiveData<Pair<A, B>> {
    val result = MediatorLiveData<Pair<A, B>>()
    result.addSource(this) { a ->
        if (a != null && stream.value != null) {
            result.value = Pair(a, stream.value!!)
        }
    }
    result.addSource(stream) { b ->
        if (b != null && this.value != null) {
            result.value = Pair(this.value!!, b)
        }
    }
    return result
}

class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val mPending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        if (hasActiveObservers()) {

        }

        // Observe the internal MutableLiveData
        super.observe(owner, Observer<T> { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(@Nullable t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }
}