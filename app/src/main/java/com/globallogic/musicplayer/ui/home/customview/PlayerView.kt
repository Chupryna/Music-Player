package com.globallogic.musicplayer.ui.home.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import com.globallogic.musicplayer.R
import com.globallogic.musicplayer.data.model.Audio
import com.globallogic.musicplayer.databinding.VPlayerBinding
import com.globallogic.musicplayer.service.notification.NotificationStrategy.Companion.NEXT
import com.globallogic.musicplayer.service.notification.NotificationStrategy.Companion.PAUSE
import com.globallogic.musicplayer.service.notification.NotificationStrategy.Companion.PLAY
import com.globallogic.musicplayer.service.notification.NotificationStrategy.Companion.PREVIOUS
import com.globallogic.musicplayer.ui.player.EmptySeekBarChangeListener
import kotlinx.android.synthetic.main.v_player.view.*

class PlayerView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

	var callback: OnTrackProgressListener? = null

	private val binding: VPlayerBinding = VPlayerBinding.inflate(LayoutInflater.from(context),this, true)
	val action = MutableLiveData<String>()

	init {
		val listener = OnClickListener { view ->
			when (view.id) {
				R.id.playImageView -> onPlay()
				R.id.pauseImageView -> onPause()
				R.id.nextImageView -> action.value = NEXT
				R.id.previousImageView -> action.value = PREVIOUS
			}
		}

		playImageView.setOnClickListener(listener)
		pauseImageView.setOnClickListener(listener)
		nextImageView.setOnClickListener(listener)
		previousImageView.setOnClickListener(listener)
		trackProgressSeekBar.setOnSeekBarChangeListener(object : EmptySeekBarChangeListener() {
			override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
				if (fromUser) {
					callback?.onProgressChanged(progress)
				}
			}
		})
	}

	fun updateTrack(newTrack: Audio, progress: Int) {
		binding.track = newTrack
		updateProgress(progress)
	}

	private fun updateProgress(value: Int) {
		if (value > 0) {
			trackProgressSeekBar.max = value
		}
	}

	fun onPause(){
		action.value = PAUSE
		binding.isPlay = false
	}

	fun onPlay() {
		action.value = PLAY
		binding.isPlay = true
	}

	fun updateTrackProgress(progress: Int) {
		trackProgressSeekBar.progress = progress
	}

	interface OnTrackProgressListener {
		fun onProgressChanged(progress: Int)
	}
}