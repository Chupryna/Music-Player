package com.globallogic.musicplayer.util

import android.os.Bundle
import androidx.fragment.app.Fragment

inline fun <T : Fragment> T.updateArguments(block: Bundle.() -> Unit) = apply {
	arguments = Bundle().also(block)
}