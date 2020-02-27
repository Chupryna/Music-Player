package com.globallogic.musicplayer.service.player

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.os.Binder
import androidx.lifecycle.MutableLiveData
import com.globallogic.musicplayer.manager.SharedPreferenceManager
import com.globallogic.musicplayer.data.AudioRepository
import com.globallogic.musicplayer.data.model.Audio
import com.globallogic.musicplayer.service.notification.NotificationFactory
import com.globallogic.musicplayer.service.notification.NotificationStrategy
import com.globallogic.musicplayer.ui.BaseService
import com.globallogic.musicplayer.service.notification.NotificationStrategy.Companion.ACTION_PAUSE
import com.globallogic.musicplayer.service.notification.NotificationStrategy.Companion.ACTION_PLAY
import com.globallogic.musicplayer.service.notification.NotificationStrategy.Companion.CLOSE
import com.globallogic.musicplayer.service.notification.NotificationStrategy.Companion.NEXT
import com.globallogic.musicplayer.service.notification.NotificationStrategy.Companion.PAUSE
import com.globallogic.musicplayer.service.notification.NotificationStrategy.Companion.PLAY
import com.globallogic.musicplayer.service.notification.NotificationStrategy.Companion.PREVIOUS
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AudioService : BaseService(), MediaPlayer.OnPreparedListener {

	companion object {
		private const val TRACK_INDEX_EXTRAS = "TRACK_INDEX_EXTRAS"
		const val NOTIFICATION_ID = 1

		fun createIntent(context: Context, index: Int) = Intent(context, AudioService::class.java).apply {
			putExtra(TRACK_INDEX_EXTRAS, index)
		}
	}

	private lateinit var notificationStrategy: NotificationStrategy
	private lateinit var notification: Notification

	private val repository = AudioRepository()
	private val localBinder = MyBinder()
	private var isPrepared = false

	val isPlaying = MutableLiveData<Boolean>(true)
	val currentTrack = MutableLiveData<Audio>()
	val duration = MutableLiveData<Long>()
	val isPlaylistEnd = MutableLiveData<Boolean>(false)
	val mediaPlayer = MediaPlayer()

	override fun onCreate() {
		super.onCreate()

		notificationStrategy = NotificationFactory.createNotificationStrategy(this)
		initializePlayer()
	}

	override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
		if (intent.action != null) {
			handleIntentAction(intent)
			return START_NOT_STICKY
		}

		val trackIndex = intent.getIntExtra(TRACK_INDEX_EXTRAS, -1)
		if (trackIndex < 0) {
			stopSelf()
			return super.onStartCommand(intent, flags, startId)
		}
		loadTrackByIndex(trackIndex)

		notification = notificationStrategy.createNotification(Audio())
		startForeground(NOTIFICATION_ID, notification)

		return START_NOT_STICKY
	}

	override fun onBind(intent: Intent): IBinder {
		return localBinder
	}

	override fun onDestroy() {
		super.onDestroy()

		savePlayerState()
		notificationStrategy.removeNotification()
		mediaPlayer.release()
	}

	private fun savePlayerState() {
		val track = currentTrack.value ?: return
		SharedPreferenceManager.putInt(SharedPreferenceManager.TRACK_INDEX, track.index)
		SharedPreferenceManager.putInt(SharedPreferenceManager.TRACK_PROGRESS, mediaPlayer.currentPosition)
		SharedPreferenceManager.putInt(SharedPreferenceManager.TRACK_DURATION, mediaPlayer.duration)
	}

	override fun onPrepared(player: MediaPlayer) {
		duration.value = player.duration.toLong()
		player.start()
		isPlaying.value = true
	}

	private fun initializePlayer() {
		mediaPlayer.setOnPreparedListener(this)
		mediaPlayer.setOnCompletionListener {
			loadNextTrack()
		}
	}

	private fun handleIntentAction(intent: Intent) {
		when(intent.action) {
			PAUSE -> pause()
			PLAY -> reset()
			NEXT -> loadNextTrack()
			PREVIOUS -> loadPreviousTrack()
			CLOSE -> {
				pause()
				stopForeground(true)
			}
		}
	}

	fun pause() {
		val playing = isPlaying.value ?: false
		if (!playing) {
			return
		}

		mediaPlayer.pause()
		isPlaying.value = false
		notificationStrategy.updateAction(notification, ACTION_PAUSE)
	}

	fun reset() {
		val playing = isPlaying.value ?: false
		if (playing) {
			return
		}

		if (!isPrepared) {
			mediaPlayer.prepareAsync()
		} else {
			mediaPlayer.start()
			isPlaying.value = true
		}
		notificationStrategy.updateAction(notification, ACTION_PLAY)
	}

	fun loadNextTrack() {
		val track = currentTrack.value
		if (track != null) {
			loadTrackByIndex(track.index + 1)
		}
	}

	fun loadPreviousTrack() {
		val track = currentTrack.value ?: return

		val index = track.index
		if (index > 0) {
			loadTrackByIndex(index-1)
		} else if (index == 0) {
			mediaPlayer.seekTo(0)
		}
	}

	private fun loadTrackByIndex(newIndex: Int) {
		repository.getAudioFromDevice(contentResolver, 1, newIndex)
			.subscribeOn(Schedulers.newThread())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { result: ArrayList<Audio> ->
				if (result.isNotEmpty()) {
					isPlaylistEnd.value = false
					val track = result.firstOrNull() ?: return@subscribe
					track.index = newIndex
					currentTrack.value = track
					notification = notificationStrategy.createNotification(track)
					updateTrack(track)
				} else {
					isPlaylistEnd.value = true
					mediaPlayer.seekTo(0)
				}
			}.addDisposable()
	}

	private fun updateTrack(audio: Audio) {
		mediaPlayer.reset()
		mediaPlayer.setDataSource(audio.path)
		val playing = isPlaying.value ?: false
		if (playing) {
			mediaPlayer.prepareAsync()
			isPrepared = true
		} else {
			isPrepared = false
			notificationStrategy.updateAction(notification, ACTION_PAUSE)
		}
	}

	fun updateCurrentTime(progress: Int) {
		mediaPlayer.seekTo(progress)
	}

	inner class MyBinder : Binder() {
		val service: AudioService
			get() = this@AudioService
	}
}