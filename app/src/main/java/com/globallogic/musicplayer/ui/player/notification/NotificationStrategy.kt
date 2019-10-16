package com.globallogic.musicplayer.ui.player.notification

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import com.globallogic.musicplayer.data.model.Audio

abstract class NotificationStrategy(val context: Context) {
	companion object {
		const val PAUSE = "PAUSE_ACTION"
		const val PLAY = "PLAY_ACTION"
		const val PREVIOUS = "PREVIOUS_ACTION"
		const val NEXT = "NEXT_ACTION"
		const val LIKE = "LIKE_ACTION"

		const val ACTION_PLAY = 1
		const val ACTION_PAUSE = 2
	}

	protected val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

	abstract fun createNotification(track: Audio): Notification
	abstract fun removeNotification()
	abstract fun updateAction(notification: Notification, action: Int)
}