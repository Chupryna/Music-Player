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
import com.globallogic.musicplayer.util.updateArguments
import org.koin.android.viewmodel.ext.android.viewModel

class TrackListFragment : BindingFragment<FTracksListBinding>() {

	enum class Pages(val position: Int) { FOLDERS_PAGE(0), ALL_TRACKS_PAGE(1), FAVOURITE_TRACKS_PAGE(2) }

	companion object {
		private const val ARG_PAGE_NUMBER = "page_number"

		fun newInstance(pageNumber: Int) = TrackListFragment().updateArguments {
			putInt(ARG_PAGE_NUMBER, pageNumber)
		}
	}

	private lateinit var adapter: TrackAdapter

	private val model by viewModel<TrackListViewModel>()
	private var isLastPage = false
	private var isLoading = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		adapter = TrackAdapter(model, layoutInflater)
		model.initialize()
		model.event.observe(this, Observer {
			if (it is TrackListViewModel.Event.OnTrackSelectedEvent) {
				startActivity(PlayerActivity.createIntent(requireContext(), it.index).setAction(START_SERVICE_ACTION))
			}
		})
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

		model.tracksList.observe(this, Observer<List<Audio>> {
			if (it.size <= adapter.itemCount && adapter.itemCount != 0) {
				isLastPage = true
				adapter.removeLoading()
				return@Observer
			}

			isLoading = false
			adapter.updateList(it)
		})
	}
}