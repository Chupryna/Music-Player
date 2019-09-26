package com.globallogic.musicplayer.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

	public val isMusicExists = MutableLiveData<Boolean>(false)
}