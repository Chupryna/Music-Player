package com.globallogic.musicplayer.util

import java.util.concurrent.TimeUnit

class TimeConverter {
	companion object {
		fun getTrackDuration(time: Long): String {
			return String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(time),
				TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)))
		}
	}
}