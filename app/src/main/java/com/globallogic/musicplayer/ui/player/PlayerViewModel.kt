package com.globallogic.musicplayer.ui.player

import androidx.lifecycle.MutableLiveData
import com.globallogic.musicplayer.data.AudioRepository
import com.globallogic.musicplayer.data.model.Audio
import com.globallogic.musicplayer.ui.BaseViewModel

class PlayerViewModel(private val repository: AudioRepository) : BaseViewModel() {
	sealed class Event {
		object LoadPreviousTrack : Event()
		object LoadNextTrack : Event()
	}

	val isPlay = MutableLiveData<Boolean>()
	val track = MutableLiveData<Audio>()
	val event = MutableLiveData<Event>()
	val isFavouriteTrack = MutableLiveData(false)

	fun onPlay() {
		isPlay.value = true
	}

	fun onPause() {
		isPlay.value = false
	}

	fun onPrevious() {
		event.value = Event.LoadPreviousTrack
	}

	fun onNext() {
		event.value = Event.LoadNextTrack
	}

	fun updateTrack(newTrack: Audio) {
		track.value = newTrack
		isFavouriteTrack.value = newTrack.isFavourite
	}

	fun setPlay(value: Boolean) {
		if (isPlay.value != value) {
			isPlay.value = value
		}
	}

	fun onLike() {
		val isFavourite = isFavouriteTrack.value ?: false
		isFavouriteTrack.value = !isFavourite

		val track = track.value ?: return
		repository.saveFavouriteTrack(track).addDisposable()
	}
}