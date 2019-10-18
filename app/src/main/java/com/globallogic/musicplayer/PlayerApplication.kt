package com.globallogic.musicplayer

import android.app.Application
import com.globallogic.musicplayer.manager.SharedPreferenceManager

class PlayerApplication : Application() {
	override fun onCreate() {
		super.onCreate()

		SharedPreferenceManager.init(this)
	}
}