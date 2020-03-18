package com.globallogic.musicplayer.ui.home.favouriteTracks

import androidx.lifecycle.MutableLiveData
import com.globallogic.musicplayer.data.AudioRepository
import com.globallogic.musicplayer.data.model.Audio
import com.globallogic.musicplayer.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * @author Anatolii Chupryna.
 */

class FavouriteTracksViewModel(repository: AudioRepository) : BaseViewModel() {
	val tracks = MutableLiveData<List<Audio>>()
	val hasTracks = MutableLiveData(false)

	init {
		repository.trackFavouriteTracks()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe{
				tracks.value = it
				hasTracks.value = it.isNotEmpty()
			}
			.addDisposable()
	}
}