package com.globallogic.musicplayer.ui.home.tracksList

import android.content.ContentResolver
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.globallogic.musicplayer.data.AudioRepository
import com.globallogic.musicplayer.data.model.Audio
import com.globallogic.musicplayer.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TrackListViewModel(private val repository: AudioRepository,
						 private val contentResolver: ContentResolver) : BaseViewModel() {

	companion object {
		private const val TAG = "TrackListViewModel"
	}

	sealed class Event {
		data class OnTrackSelectedEvent(val index: Int) : Event()
	}

	val isMusicExists = MutableLiveData<Boolean>(false)
	val tracksList = MutableLiveData<ArrayList<Audio>>(ArrayList())
	val event = MutableLiveData<Event>()

	fun	initialize() {
		loadAudio()
	}

	fun loadAudio(offset: Int = 0) {
		repository.getAudioFromDevice(contentResolver, AudioRepository.LIMIT, offset)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { results: List<Audio>, throwable: Throwable? ->
				if (throwable != null) {
					Log.e(TAG, "Error: " + throwable.message)
					return@subscribe
				}

				val newList = tracksList.value?.apply { addAll(results) } ?: ArrayList(results)
				tracksList.value = newList

				isMusicExists.value = newList.isNotEmpty()
			}.addDisposable()
	}

	fun onSelectTrack(index: Int) {
		event.value = Event.OnTrackSelectedEvent(index)
	}
}