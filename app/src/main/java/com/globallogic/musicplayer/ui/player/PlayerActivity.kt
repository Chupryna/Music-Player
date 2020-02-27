package com.globallogic.musicplayer.ui.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.SeekBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.globallogic.musicplayer.R
import com.globallogic.musicplayer.databinding.APlayerBinding
import com.globallogic.musicplayer.service.player.AudioService
import com.globallogic.musicplayer.util.TimeConverter.Companion.getTrackDuration
import java.util.concurrent.TimeUnit

class PlayerActivity : AppCompatActivity() {

	companion object {
		private const val TAG = "PlayerActivity"
		private const val SELECTED_AUDIO_INDEX_EXTRAS = "SELECTED_AUDIO_INDEX_EXTRAS"
		const val START_SERVICE_ACTION = "START_SERVICE_ACTION"

		fun createIntent(context: Context, index: Int) =
			Intent(context, PlayerActivity::class.java).apply {
				putExtra(SELECTED_AUDIO_INDEX_EXTRAS, index)
			}
	}

	private lateinit var audioService: AudioService
	private lateinit var model: PlayerViewModel

	private var binding: APlayerBinding? = null
	private val handler = Handler(Looper.getMainLooper())
	private var isBound = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val localBinding = APlayerBinding.inflate(layoutInflater)
		setContentView(localBinding.root)
		model = ViewModelProviders.of(this).get(PlayerViewModel::class.java)
		localBinding.lifecycleOwner = this
		localBinding.model = model
		binding = localBinding

		val selectedAudioIndex = intent.getIntExtra(SELECTED_AUDIO_INDEX_EXTRAS, -1)
		if (selectedAudioIndex < 0) {
			Log.d(TAG, "selectedAudioIndex < 0")
			return
		}

		if (intent.action == START_SERVICE_ACTION) {
			startService(AudioService.createIntent(this, selectedAudioIndex))
		}
		bindService(Intent(this, AudioService::class.java), serviceConnection, 0)

		localBinding.trackProgressSeekBar.setOnSeekBarChangeListener(object :
			EmptySeekBarChangeListener() {
			override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
				if (fromUser) {
					audioService.updateCurrentTime(progress * 1000)
				}
			}
		})

		model.event.observe(this, Observer(::onViewModelEvent))
	}

	override fun onDestroy() {
		super.onDestroy()

		if (isBound) {
			unbindService(serviceConnection)
			isBound = false
			handler.removeCallbacksAndMessages(null)
		}
	}

	private fun onViewModelEvent(event: PlayerViewModel.Event) {
		when (event) {
			PlayerViewModel.Event.LoadNextTrack -> audioService.loadNextTrack()
			PlayerViewModel.Event.LoadPreviousTrack -> audioService.loadPreviousTrack()
		}
	}

	private val serviceConnection = object : ServiceConnection {
		override fun onServiceDisconnected(name: ComponentName) {
			isBound = false
			handler.removeCallbacksAndMessages(null)
		}

		override fun onServiceConnected(name: ComponentName, service: IBinder) {
			val binderBridge = service as AudioService.MyBinder
			audioService = binderBridge.service
			isBound = true

			model.isPlay.observe(this@PlayerActivity, Observer(::onPlayerEventChanged))
			audioService.currentTrack.observe(this@PlayerActivity, Observer { model.updateTrack(it) })
			audioService.duration.observe(this@PlayerActivity, Observer(::updateTrackDuration))
			audioService.isPlaylistEnd.observe(this@PlayerActivity, Observer(::onPlaylistEnd))
			audioService.isPlaying.observe(this@PlayerActivity, Observer { model.setPlay(it) })

			handler.post(updateTrackTime)
		}
	}

	private fun onPlayerEventChanged(isPlay: Boolean) {
		if (isPlay) {
			audioService.reset()
			handler.post(updateTrackTime)
		} else {
			audioService.pause()
			handler.removeCallbacksAndMessages(null)
		}
	}

	private fun updateTrackDuration(time: Long) {
		val localBinding = binding ?: return
		localBinding.trackEndTimeTextView.text = getTrackDuration(time)
		localBinding.trackProgressSeekBar.max = TimeUnit.MILLISECONDS.toSeconds(time).toInt()
	}

	private fun onPlaylistEnd(isEnd: Boolean) {
		val localBinding = binding ?: return
		if (isEnd) {
			model.isPlay.value = false
			localBinding.trackProgressSeekBar.progress = 0
			localBinding.trackCurrentTimeTextView.text = getString(R.string.startTime)
		}
	}

	private val updateTrackTime = object : Runnable {
		override fun run() {
			val localBinding = binding ?: return
			val time = audioService.mediaPlayer.currentPosition
			localBinding.trackCurrentTimeTextView.text = getTrackDuration(time.toLong())
			localBinding.trackProgressSeekBar.progress = time / 1000
			handler.postDelayed(this, 500)
		}
	}
}