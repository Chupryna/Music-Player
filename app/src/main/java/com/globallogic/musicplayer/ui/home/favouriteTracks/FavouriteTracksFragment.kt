package com.globallogic.musicplayer.ui.home.favouriteTracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
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

	private lateinit var adapter: FavouriteTracksAdapter

	private val model by viewModel<FavouriteTracksViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		adapter = FavouriteTracksAdapter(layoutInflater)
		model.tracks.observe(this, Observer(adapter::update))
	}

	override fun onCreateBinding(inflater: LayoutInflater, container: ViewGroup?): FFavouriteTracksBinding {
		return FFavouriteTracksBinding.inflate(inflater, container, false).also {
			it.model = model
		}
	}

	override fun onBindingCreated(binding: FFavouriteTracksBinding, savedInstanceState: Bundle?) {
		super.onBindingCreated(binding, savedInstanceState)

		binding.recycler.layoutManager = LinearLayoutManager(requireContext())
	}
}