package com.globallogic.musicplayer.service.notification

import android.content.Context
import android.os.Build

object NotificationFactory {
	fun createNotificationStrategy(context: Context) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) OreoNotificationStrategy(context)
		else PreOreoNotificationStrategy(context)
}
