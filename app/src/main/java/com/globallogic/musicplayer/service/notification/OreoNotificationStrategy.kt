package com.globallogic.musicplayer.service.notification

import android.annotation.TargetApi
import android.app.Notification
import android.app.Notification.FLAG_ONGOING_EVENT
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.media.session.MediaSession
import android.os.Build
import com.globallogic.musicplayer.R
import com.globallogic.musicplayer.data.model.Audio
import com.globallogic.musicplayer.ui.player.AudioService
import com.globallogic.musicplayer.ui.player.AudioService.Companion.NOTIFICATION_ID
import com.globallogic.musicplayer.ui.player.PlayerActivity
import com.globallogic.musicplayer.service.notification.NotificationChannelManager.Companion.CHANNEL_ID

@TargetApi(Build.VERSION_CODES.O)
class OreoNotificationStrategy(context: Context) : NotificationStrategy(context) {

	private val notificationChannelManager = NotificationChannelManager(context)

	override fun createNotification(track: Audio): Notification {
		val pausePendingIntent = PendingIntent.getService(context, 0, Intent(context, AudioService::class.java).setAction(PAUSE), 0)
		val previousPendingIntent = PendingIntent.getService(context, 0, Intent(context, AudioService::class.java).setAction(PREVIOUS), 0)
		val nextPendingIntent = PendingIntent.getService(context, 0, Intent(context, AudioService::class.java).setAction(NEXT), 0)
		val likePendingIntent = PendingIntent.getService(context, 0, Intent(context, AudioService::class.java).setAction(LIKE), 0)
		val closePendingIntent = PendingIntent.getService(context, 0, Intent(context, AudioService::class.java).setAction(CLOSE), 0)
		val openActivityPendingIntent = PendingIntent.getActivity(context, 0, PlayerActivity.createIntent(context, track.index).setAction(OPEN), 0)

		val pauseIcon = Icon.createWithResource(context, R.drawable.ic_pause)
		val nextIcon = Icon.createWithResource(context, R.drawable.ic_skip_next)
		val previousIcon = Icon.createWithResource(context, R.drawable.ic_skip_previous)
		val likeIcon = Icon.createWithResource(context, R.drawable.ic_favorite_border)
		val closeIcon = Icon.createWithResource(context, R.drawable.ic_close)

		val style = Notification.DecoratedMediaCustomViewStyle()
			.setShowActionsInCompactView(1, 2, 3)
			.setMediaSession(MediaSession(context, "OreoNotificationStrategy").sessionToken)

		if (!notificationChannelManager.isChannelExists()) {
			notificationManager.createNotificationChannel(notificationChannelManager.newNotificationChannel(context.getString(R.string.playing)))
		}

		val notification = Notification.Builder(context, CHANNEL_ID)
			.setSmallIcon(R.drawable.ic_play_arrow)
			.setLargeIcon(
				if (track.image == null) BitmapFactory.decodeResource(context.resources, R.drawable.player)
				else BitmapFactory.decodeByteArray(track.image, 0, track.image.size)
			)
			.setStyle(style)
			.setContentTitle(track.name)
			.setContentText(track.artist)
			.setContentIntent(openActivityPendingIntent)
			.setShowWhen(false)
			.setNumber(0)
			.setColorized(true)
			.addAction(Notification.Action.Builder(likeIcon, context.getString(R.string.like), likePendingIntent).build())
			.addAction(Notification.Action.Builder(previousIcon, context.getString(R.string.previous), previousPendingIntent).build())
			.addAction(Notification.Action.Builder(pauseIcon, context.getString(R.string.pause), pausePendingIntent).build())
			.addAction(Notification.Action.Builder(nextIcon, context.getString(R.string.next), nextPendingIntent).build())
			.addAction(Notification.Action.Builder(closeIcon, context.getString(R.string.next), closePendingIntent).build())
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
				notification.actions[2] = Notification.Action.Builder(Icon.createWithResource(context, R.drawable.ic_play_arrow), context.getString(R.string.play), pendingIntent).build()
				notification.flags = notification.flags and FLAG_ONGOING_EVENT.inv()
			}
			ACTION_PLAY -> {
				val pendingIntent = PendingIntent.getService(context, 0, Intent(context, AudioService::class.java).setAction(PAUSE), 0)
				notification.actions[2] = Notification.Action.Builder(Icon.createWithResource(context, R.drawable.ic_pause), context.getString(R.string.pause), pendingIntent).build()
				notification.flags = notification.flags or FLAG_ONGOING_EVENT
			}
			else -> return
		}

		notificationManager.notify(NOTIFICATION_ID, notification)
	}
}