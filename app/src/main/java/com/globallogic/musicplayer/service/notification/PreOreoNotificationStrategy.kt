package com.globallogic.musicplayer.service.notification

import android.app.Notification
import android.app.Notification.FLAG_ONGOING_EVENT
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.globallogic.musicplayer.R
import com.globallogic.musicplayer.data.model.Audio
import com.globallogic.musicplayer.ui.player.AudioService
import com.globallogic.musicplayer.ui.player.AudioService.Companion.NOTIFICATION_ID
import com.globallogic.musicplayer.ui.player.PlayerActivity

class PreOreoNotificationStrategy(context: Context) : NotificationStrategy(context) {

	override fun createNotification(track: Audio): Notification {
		val pausePendingIntent = PendingIntent.getService(context, 0, Intent(context, AudioService::class.java).setAction(PAUSE), 0)
		val previousPendingIntent = PendingIntent.getService(context, 0, Intent(context, AudioService::class.java).setAction(PREVIOUS), 0)
		val nextPendingIntent = PendingIntent.getService(context, 0, Intent(context, AudioService::class.java).setAction(NEXT), 0)
		val openActivityPendingIntent = PendingIntent.getActivity(context, 0, PlayerActivity.createIntent(context, track.index), 0)

		val notification = NotificationCompat.Builder(context, "")
			.setSmallIcon(R.drawable.ic_play_arrow)
			.setLargeIcon(
				if (track.image == null) BitmapFactory.decodeResource(context.resources, R.drawable.player)
				else BitmapFactory.decodeByteArray(track.image, 0, track.image.size)
			)
			.setColor(ContextCompat.getColor(context, R.color.colorPrimary))
			.setContentTitle(track.name)
			.setContentText(track.artist)
			.setContentIntent(openActivityPendingIntent)
			.setShowWhen(false)
			.setNumber(0)
			.addAction(NotificationCompat.Action(R.drawable.ic_skip_previous, context.getString(R.string.previous), previousPendingIntent))
			.addAction(NotificationCompat.Action(R.drawable.ic_pause, context.getString(R.string.pause), pausePendingIntent))
			.addAction(NotificationCompat.Action(R.drawable.ic_skip_next, context.getString(R.string.next), nextPendingIntent))
			.build()

		notificationManager.notify(NOTIFICATION_ID, notification)
		return notification
	}

	override fun removeNotification() {
		notificationManager.cancel(NOTIFICATION_ID)
	}

	override fun updateAction(notification: Notification, action: Int) {
		when (action) {
			ACTION_PAUSE -> {
				val pendingIntent = PendingIntent.getService(context, 0, Intent(context, AudioService::class.java).setAction(PLAY), 0)
				notification.actions[2] = Notification.Action(R.drawable.ic_play_arrow, context.getString(R.string.play), pendingIntent)
				notification.flags = notification.flags and FLAG_ONGOING_EVENT.inv()
			}
			ACTION_PLAY -> {
				val pendingIntent = PendingIntent.getService(context, 0, Intent(context, AudioService::class.java).setAction(PAUSE), 0)
				notification.actions[2] = Notification.Action(R.drawable.ic_pause, context.getString(R.string.pause), pendingIntent)
				notification.flags = notification.flags or FLAG_ONGOING_EVENT
			}
			else -> return
		}

		notificationManager.notify(NOTIFICATION_ID, notification)
	}
}