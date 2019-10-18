package com.globallogic.musicplayer.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.globallogic.musicplayer.R
import com.globallogic.musicplayer.data.model.Audio
import com.globallogic.musicplayer.databinding.VhTrackBinding
import com.globallogic.musicplayer.ui.home.TrackListViewModel
import java.util.ArrayList

private const val VIEW_TYPE_LOADING = 0
private const val VIEW_TYPE_AUDIO = 1

class TrackAdapter(private val model: TrackListViewModel, private val layoutInflater: LayoutInflater) :
	RecyclerView.Adapter<TrackAdapter.BaseViewHolder>() {

	init {
		setHasStableIds(true)
	}

	private var items = ArrayList<Item>()
	private var itemId = 0L
	private var isLoadingMore = false

	override fun getItemCount(): Int {
		return items.size
	}

	override fun getItemId(position: Int): Long {
		return items[position].id
	}

	override fun getItemViewType(position: Int) = if (isLoadingMore) {
		if (position == items.size - 1) VIEW_TYPE_LOADING else VIEW_TYPE_AUDIO
	} else {
		VIEW_TYPE_AUDIO
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
		return when (viewType) {
			VIEW_TYPE_LOADING -> LoadingViewHolder(layoutInflater.inflate(R.layout.vh_loading, parent, false))
			VIEW_TYPE_AUDIO -> TrackViewHolder(VhTrackBinding.inflate(layoutInflater, parent, false))
			else -> TrackViewHolder(VhTrackBinding.inflate(layoutInflater, parent, false))
		}
	}

	override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
		holder.onBind(model, items[position])
	}

	fun updateList(list: List<Audio>) {
		val map = items.associateBy { it.audio.id }
		items = list.mapTo(ArrayList(), {
			Item(
				id = map[it.id]?.id ?: ++itemId,
				audio = it
			)
		})

		notifyDataSetChanged()
	}

	fun addLoading() {
		isLoadingMore = true
		items.add(Item())
		notifyItemInserted(items.size - 1)
	}

	fun removeLoading() {
		isLoadingMore = false
		val position = items.size - 1
		items.removeAt(position)
		notifyItemRemoved(position)
	}

	inner class Item(val id: Long = 0, val audio: Audio = Audio())

	abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		abstract fun onBind(model: TrackListViewModel, item: Item)
	}

	inner class TrackViewHolder(private val binding: VhTrackBinding) : BaseViewHolder(binding.root) {
		override fun onBind(model: TrackListViewModel, item: Item) {
			binding.item = item.audio
			binding.model = model
			binding.index = adapterPosition
		}
	}

	inner class LoadingViewHolder(view: View) : BaseViewHolder(view) {
		override fun onBind(model: TrackListViewModel, item: Item) {}
	}
}