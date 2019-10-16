package com.globallogic.musicplayer.ui.player

import android.widget.SeekBar

open class EmptySeekBarChangeListener : SeekBar.OnSeekBarChangeListener {
	override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
	}

	override fun onStartTrackingTouch(seekBar: SeekBar) {
	}

	override fun onStopTrackingTouch(seekBar: SeekBar) {
	}
}