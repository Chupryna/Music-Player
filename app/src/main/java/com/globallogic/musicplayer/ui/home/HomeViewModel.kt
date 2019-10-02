package com.globallogic.musicplayer.ui.home

import android.content.ContentResolver
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.globallogic.musicplayer.data.AudioRepository
import com.globallogic.musicplayer.data.model.Audio
import com.globallogic.musicplayer.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel : BaseViewModel() {

	companion object {
		private const val TAG = "HomeViewModel"
	}

	public val isMusicExists = MutableLiveData<Boolean>(false)
	public val tracksList = MutableLiveData<ArrayList<Audio>>(ArrayList())
	public val audioRepository = AudioRepository()

	public fun loadAudio(contentResolver: ContentResolver, offset: Int = 0) {
		audioRepository.getAudioFromDevice(contentResolver, offset)
			.subscribeOn(Schedulers.newThread())
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

	public fun getTrackAt(index: Int): Audio? {
		if (tracksList.value != null)
			return null
		if (index < 0 && index >= tracksList.value!!.size)
			return null

		return tracksList.value!![index]
	}
}