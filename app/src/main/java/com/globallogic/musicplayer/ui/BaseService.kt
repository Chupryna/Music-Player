package com.globallogic.musicplayer.ui

import android.app.Service
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseService : Service() {
	private val compositeDisposable = CompositeDisposable()

	fun <T : Disposable> T.addDisposable() {
		compositeDisposable.add(this)
	}

	override fun onDestroy() {
		compositeDisposable.dispose()

		super.onDestroy()
	}
}