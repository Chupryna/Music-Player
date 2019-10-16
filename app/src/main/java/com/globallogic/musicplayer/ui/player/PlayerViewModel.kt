package com.globallogic.musicplayer.ui.player

import androidx.lifecycle.MutableLiveData
import com.globallogic.musicplayer.ui.BaseViewModel

class PlayerViewModel : BaseViewModel() {
	val isPlay = MutableLiveData<Boolean>()

	fun onPlay() {
		isPlay.value = true
	}

	fun onPause() {
		isPlay.value = false
	}
}