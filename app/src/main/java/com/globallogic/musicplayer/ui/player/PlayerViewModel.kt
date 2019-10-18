package com.globallogic.musicplayer.ui.player

import androidx.lifecycle.MutableLiveData
import com.globallogic.musicplayer.data.model.Audio
import com.globallogic.musicplayer.ui.BaseViewModel

class PlayerViewModel : BaseViewModel() {
	val isPlay = MutableLiveData<Boolean>()
	val track = MutableLiveData<Audio>()

	fun onPlay() {
		isPlay.value = true
	}

	fun onPause() {
		isPlay.value = false
	}

	fun updateTrack(newTrack: Audio) {
		track.value = newTrack
	}
}