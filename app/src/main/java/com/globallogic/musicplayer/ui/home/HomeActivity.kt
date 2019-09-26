package com.globallogic.musicplayer.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.globallogic.musicplayer.R
import com.globallogic.musicplayer.ui.home.adapter.SectionsPagerAdapter
import kotlinx.android.synthetic.main.a_home.*

class HomeActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.a_home)

		viewPager.adapter = SectionsPagerAdapter(resources, supportFragmentManager)
		tabs.setupWithViewPager(viewPager)
	}
}