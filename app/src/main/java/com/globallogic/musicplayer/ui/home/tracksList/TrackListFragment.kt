package com.globallogic.musicplayer.ui.home.tracksList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.globallogic.musicplayer.data.model.Audio
import com.globallogic.musicplayer.databinding.FTracksListBinding
import com.globallogic.musicplayer.ui.BindingFragment
import com.globallogic.musicplayer.ui.home.adapter.TrackAdapter
import com.globallogic.musicplayer.ui.player.PlayerActivity
import com.globallogic.musicplayer.ui.player.PlayerActivity.Companion.START_SERVICE_ACTION
import org.koin.android.viewmodel.ext.android.viewModel

class TrackListFragment : BindingFragment<FTracksListBinding>() {

	companion object {
		fun newInstance() = TrackListFragment()
	}

	private lateinit var adapter: TrackAdapter

	private val model by viewModel<TrackListViewModel>()
	private var isLastPage = false
	private var isLoading = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		adapter = TrackAdapter(model, layoutInflater)
		model.initialize()
		model.event.observe(this, Observer(::onViewModelEvent))
	}

	override fun onCreateBinding(inflater: LayoutInflater, container: ViewGroup?): FTracksListBinding {
		return FTracksListBinding.inflate(inflater, container, false).also {
			it.model = model
		}
	}

	override fun onBindingCreated(binding: FTracksListBinding, savedInstanceState: Bundle?) {
		val linearLayoutManager = LinearLayoutManager(context)
		binding.recyclerView.layoutManager = linearLayoutManager
		binding.recyclerView.adapter = adapter
		binding.recyclerView.addOnScrollListener(object : TrackLoaderListener(linearLayoutManager) {
			override fun loadMoreItems() {
				if (isLastPage) {
					return
				}

				adapter.addLoading()
				isLoading = true
				model.loadAudio(adapter.itemCount)
			}

			override fun isLastPage(): Boolean {
				return isLastPage
			}

			override fun isLoading(): Boolean {
				return isLoading
			}
		})

		model.tracksList.observe(this, Observer(::onTracksListUpdated))
	}

	private fun onViewModelEvent(event: TrackListViewModel.Event) {
		if (event is TrackListViewModel.Event.OnTrackSelectedEvent) {
			startActivity(PlayerActivity.createIntent(requireContext(), event.index).setAction(START_SERVICE_ACTION))
		}
	}

	private fun onTracksListUpdated(list: List<Audio>) {
		if (list.size <= adapter.itemCount && adapter.itemCount != 0) {
			isLastPage = true
			adapter.removeLoading()
			return
		}

		isLoading = false
		adapter.updateList(list)
	}
}