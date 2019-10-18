package com.globallogic.musicplayer.ui.home.adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.globallogic.musicplayer.R
import com.globallogic.musicplayer.ui.home.TrackListFragment

class TabsPagerAdapter(resources: Resources, fragmentManager: FragmentManager) :
	FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

	companion object {
		private const val COUNT_TABS = 3
	}

	private val pageTitles: Array<String> = resources.getStringArray(R.array.tabs)

	override fun getItem(position: Int): Fragment {
		return TrackListFragment.newInstance(position)
	}

	override fun getPageTitle(position: Int): CharSequence? {
		return pageTitles[position]
	}

	override fun getCount(): Int {
		return COUNT_TABS
	}
}