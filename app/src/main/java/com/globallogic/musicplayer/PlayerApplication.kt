package com.globallogic.musicplayer

import android.app.Application

class PlayerApplication : Application() {
	override fun onCreate() {
		super.onCreate()

		initInjection(this)
	}
}