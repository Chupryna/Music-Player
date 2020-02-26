package com.globallogic.musicplayer.ui.home

import android.content.ContentResolver
import androidx.lifecycle.MutableLiveData
import com.globallogic.musicplayer.data.AudioRepository
import com.globallogic.musicplayer.data.model.Audio
import com.globallogic.musicplayer.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel : BaseViewModel() {

	private val repository = AudioRepository()
	val lastTrack = MutableLiveData<Audio>()

	fun loadAudioByIndex(contentResolver: ContentResolver, index: Int) {
		repository.getAudioFromDevice(contentResolver, 1, index)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { result: ArrayList<Audio> ->
				val track = result.firstOrNull() ?: return@subscribe
				lastTrack.value = track
			}.addDisposable()
	}
}