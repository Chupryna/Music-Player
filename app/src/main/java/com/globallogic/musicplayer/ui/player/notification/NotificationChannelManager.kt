package com.globallogic.musicplayer.ui.player.notification

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

@TargetApi(Build.VERSION_CODES.O)
class NotificationChannelManager(val context: Context) {
	companion object {
		const val CHANNEL_ID = "PlayingChannel"
	}

	private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

	fun newNotificationChannel(name: String) =
		NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH).apply {
			enableVibration(false)
			setSound(null, null)
			setShowBadge(false)
		}

	fun isChannelExists() = notificationManager.getNotificationChannel(CHANNEL_ID) != null
}