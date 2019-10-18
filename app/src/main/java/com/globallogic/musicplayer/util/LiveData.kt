package com.globallogic.musicplayer.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class LiveData<T> : MutableLiveData<T>() {
	override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
		if (hasObservers()) {
			throw IllegalStateException("already have registered observers")
		}
		super.observe(owner, Observer { t ->
			observer.onChanged(t)
		})
	}

	inline fun observe(owner: LifecycleOwner, crossinline block: (T) -> Unit) {
		observe(owner, Observer { block(it!!) })
	}

	override fun observeForever(observer: Observer<in T>) {
		if (hasObservers()) {
			throw IllegalStateException("already have registered observers")
		}
		super.observeForever { t ->
			observer.onChanged(t)
		}
	}

	inline fun observeForever(crossinline block: (T) -> Unit) {
		observeForever(Observer { block(it!!) })
	}
}