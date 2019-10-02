package com.globallogic.musicplayer.ui

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

	private val compositeDisposable = CompositeDisposable()

	fun <T : Disposable> T.addDisposable() {
		compositeDisposable.add(this)
	}

	override fun onCleared() {
		super.onCleared()

		compositeDisposable.dispose()
	}
}