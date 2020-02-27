package com.globallogic.musicplayer.ui.home.favouriteTracks

import android.view.LayoutInflater
import android.view.ViewGroup
import com.globallogic.musicplayer.databinding.FFavouriteTracksBinding
import com.globallogic.musicplayer.ui.BindingFragment
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author Anatolii Chupryna.
 */

class FavouriteTracksFragment : BindingFragment<FFavouriteTracksBinding>() {

	companion object {
		fun newInstance() = FavouriteTracksFragment()
	}

	private val model by viewModel<FavouriteTracksViewModel>()

	override fun onCreateBinding(inflater: LayoutInflater, container: ViewGroup?): FFavouriteTracksBinding {
		return FFavouriteTracksBinding.inflate(inflater, container, false).also {
			it.model = model
		}
	}
}