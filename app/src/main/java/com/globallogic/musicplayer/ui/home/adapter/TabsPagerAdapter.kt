package com.globallogic.musicplayer.ui.home.adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.globallogic.musicplayer.R
import com.globallogic.musicplayer.ui.home.favouriteTracks.FavouriteTracksFragment
import com.globallogic.musicplayer.ui.home.tracksList.TrackListFragment

class TabsPagerAdapter(resources: Resources, fragmentManager: FragmentManager) :
	FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

	companion object {
		private const val COUNT_TABS = 3
	}

	private val pageTitles = resources.getStringArray(R.array.tabs)

	override fun getItem(position: Int) = when (position) {
		TrackListFragment.Pages.FOLDERS_PAGE.position -> Fragment()
		TrackListFragment.Pages.ALL_TRACKS_PAGE.position -> TrackListFragment.newInstance(position)
		TrackListFragment.Pages.FAVOURITE_TRACKS_PAGE.position -> FavouriteTracksFragment.newInstance()
		else -> Fragment()
	}

	override fun getPageTitle(position: Int): CharSequence? {
		return pageTitles[position]
	}

	override fun getCount(): Int {
		return COUNT_TABS
	}
}