package com.globallogic.musicplayer.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.globallogic.musicplayer.R
import com.globallogic.musicplayer.ui.home.adapter.TabsPagerAdapter
import kotlinx.android.synthetic.main.a_home.*

class HomeActivity : AppCompatActivity() {

	companion object {
		private const val PERMISSION_REQUEST_CODE = 1
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.a_home)

		viewPager.adapter = TabsPagerAdapter(resources, supportFragmentManager)
		tabs.setupWithViewPager(viewPager)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
			}
		}
	}
}