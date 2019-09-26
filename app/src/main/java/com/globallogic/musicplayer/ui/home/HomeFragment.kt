package com.globallogic.musicplayer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.globallogic.musicplayer.databinding.FHomeBinding
import com.globallogic.musicplayer.util.updateArguments

class HomeFragment : Fragment() {

	companion object {
		private const val ARG_PAGE_NUMBER = "page_number"

		fun newInstance(pageNumber: Int) = HomeFragment().updateArguments {
			putInt(ARG_PAGE_NUMBER, pageNumber)
		}
	}

	private lateinit var model: HomeViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		model = ViewModelProviders.of(this).get(HomeViewModel::class.java)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return FHomeBinding.inflate(inflater, container, false).root
	}
}