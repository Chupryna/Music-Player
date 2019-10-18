package com.globallogic.musicplayer.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object SharedPreferenceManager {
	const val TRACK_INDEX = "TRACK_INDEX"
	const val TRACK_DURATION = "TRACK_DURATION"
	const val TRACK_PROGRESS = "TRACK_PROGRESS"

	private lateinit var settings: SharedPreferences

	fun init(context: Context) {
		settings = PreferenceManager.getDefaultSharedPreferences(context)
	}

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