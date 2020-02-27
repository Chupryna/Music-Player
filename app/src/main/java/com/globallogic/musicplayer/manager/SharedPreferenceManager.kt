package com.globallogic.musicplayer.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class SharedPreferenceManager(context: Context) {
	companion object {
		const val TRACK_ID = "TRACK_ID"
		const val TRACK_DURATION = "TRACK_DURATION"
		const val TRACK_PROGRESS = "TRACK_PROGRESS"
	}

	private val settings = PreferenceManager.getDefaultSharedPreferences(context)

	private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
		val editor = edit()
		operation(editor)
		editor.apply()
	}

	fun putInt(key: String, value: Int) {
		settings.edit{
			it.putInt(key, value)
		}
	}

	fun getInt(key: String): Int {
		return settings.getInt(key, -1)
	}
}