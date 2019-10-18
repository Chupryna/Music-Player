package com.globallogic.musicplayer.ui.home

import android.Manifest
import android.content.ComponentName
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.globallogic.musicplayer.R
import com.globallogic.musicplayer.manager.SharedPreferenceManager
import com.globallogic.musicplayer.ui.home.adapter.TabsPagerAdapter
import com.globallogic.musicplayer.ui.home.customview.PlayerView
import com.globallogic.musicplayer.ui.player.AudioService
import com.globallogic.musicplayer.ui.player.PlayerActivity
import com.globallogic.musicplayer.service.notification.NotificationStrategy.Companion.NEXT
import com.globallogic.musicplayer.service.notification.NotificationStrategy.Companion.PAUSE
import com.globallogic.musicplayer.service.notification.NotificationStrategy.Companion.PLAY
import com.globallogic.musicplayer.service.notification.NotificationStrategy.Companion.PREVIOUS
import kotlinx.android.synthetic.main.a_home.*

class HomeActivity : AppCompatActivity(), PlayerView.OnTrackProgressListener {

	companion object {
		private const val PERMISSION_REQUEST_CODE = 1
	}

	private var audioService: AudioService? = null
	private lateinit var model: HomeViewModel

	private val handler = Handler(Looper.getMainLooper())
	private var isBound = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.a_home)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
			} else {
				initAdapter()
			}
		}

		model = ViewModelProviders.of(this).get(HomeViewModel::class.java)
		loadLastPlayingTrack()

		playerControlsView.callback = this
		playerControlsView.setOnClickListener {
			val service = audioService ?: return@setOnClickListener
			val track = service.currentTrack.value ?: return@setOnClickListener
			startActivity(PlayerActivity.createIntent(this, track.index))
		}
	}

	private fun loadLastPlayingTrack() {
		val indexLastPlayingTrack = SharedPreferenceManager.getInt(SharedPreferenceManager.TRACK_INDEX)
		if (indexLastPlayingTrack < 0) {
			return
		}

		model.loadAudioByIndex(contentResolver, indexLastPlayingTrack)
		model.lastTrack.observe(this, Observer {
			val duration = SharedPreferenceManager.getInt(SharedPreferenceManager.TRACK_DURATION) / 1000
			playerControlsView.updateTrack(it, duration)
		})
		val progress = SharedPreferenceManager.getInt(SharedPreferenceManager.TRACK_PROGRESS) / 1000
		playerControlsView.updateTrackProgress(progress)
	}

	override fun onStart() {
		super.onStart()

		bindService(AudioService.createIntent(this, -1), serviceConnection, 0)
	}

	override fun onStop() {
		super.onStop()

		if (isBound) {
			unbindService(serviceConnection)
			isBound = false
			handler.removeCallbacksAndMessages(null)
		}
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		if (requestCode == PERMISSION_REQUEST_CODE && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
			initAdapter()
		} else {
			Toast.makeText(this, getString(R.string.need_permission), Toast.LENGTH_LONG).show()
			finish()
		}
	}

	override fun onProgressChanged(progress: Int) {
		val service = audioService ?: return
		service.updateCurrentTime(progress * 1000)
	}

	private fun initAdapter() {
		viewPager.adapter = TabsPagerAdapter(resources, supportFragmentManager)
		tabs.setupWithViewPager(viewPager)
	}

	private val serviceConnection = object : ServiceConnection {
		override fun onServiceDisconnected(name: ComponentName?) {
			isBound = false
		}

		override fun onServiceConnected(name: ComponentName, iBinder: IBinder) {
			val binder = iBinder as AudioService.MyBinder
			val service = binder.service
			isBound = true

			service.currentTrack.observe(this@HomeActivity, Observer {
				val duration = service.mediaPlayer.duration / 1000
				playerControlsView.updateTrack(it, duration)
			})

			service.isPlaying.observe(this@HomeActivity, Observer {
				onPlayerEventChanged(it)
			})

			playerControlsView.action.observe(this@HomeActivity, Observer {
				when (it) {
					PAUSE -> service.pause()
					PLAY -> service.reset()
					NEXT -> service.loadNextTrack()
					PREVIOUS -> service.loadPreviousTrack()
				}
			})

			handler.post(updateTrackTime)
			audioService = service
		}
	}

	private fun onPlayerEventChanged(isPlaying: Boolean){
		if (isPlaying) {
			playerControlsView.onPlay()
			handler.post(updateTrackTime)
		} else {
			playerControlsView.onPause()
			handler.removeCallbacksAndMessages(null)
		}
	}

	private val updateTrackTime = object : Runnable {
		override fun run() {
			val service = audioService ?: return
			val time = service.mediaPlayer.currentPosition
			playerControlsView.updateTrackProgress(time / 1000)
			handler.postDelayed(this, 500)
		}
	}
}