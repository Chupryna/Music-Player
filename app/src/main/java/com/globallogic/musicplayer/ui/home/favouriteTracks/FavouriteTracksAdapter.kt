package com.globallogic.musicplayer.ui.home.favouriteTracks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.globallogic.musicplayer.R
import com.globallogic.musicplayer.data.model.Audio

/**
 * @author Anatolii Chupryna.
 */

class FavouriteTracksAdapter(private val layoutInflater: LayoutInflater) :
	RecyclerView.Adapter<FavouriteTracksAdapter.ViewHolder>() {

	private var items = emptyList<Item>()
	private var itemId = 0L

	init {
		setHasStableIds(true)
	}

	override fun getItemCount(): Int {
		return items.size
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(layoutInflater.inflate(R.layout.vh_track, parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.onBind(items[position])
	}

	fun update(list: List<Audio>) {
		val map = items.associateBy { it.audio.id }

		items = list.mapTo(ArrayList()) {
			Item(
				id = map[it.id]?.id ?: itemId++,
				audio = it
			)
		}
	}

	inner class Item(val id: Long = 0, val audio: Audio = Audio.Null)

	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun onBind(item: Item) {

		}
	}
}