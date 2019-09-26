package com.globallogic.musicplayer.ui.splash

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.globallogic.musicplayer.ui.home.HomeActivity

class SplashActivity : AppCompatActivity() {

	companion object {
		private const val DELAY = 1000L
	}

	private val handler = Handler(Looper.getMainLooper())

	override fun onResume() {
		super.onResume()

		handler.postDelayed({
			startActivity(Intent(this, HomeActivity::class.java))
		}, DELAY)
	}

	override fun onPause() {
		super.onPause()

		handler.removeCallbacksAndMessages(null)
	}
}