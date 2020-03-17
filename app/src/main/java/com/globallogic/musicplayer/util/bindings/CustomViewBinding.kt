package com.globallogic.musicplayer.util.bindings

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

@BindingAdapter("setImage", "placeholder", requireAll = false)
fun ImageView.bindImageView(image: ByteArray?, placeholder: Drawable?) {
	if (image == null) {
		if (placeholder != null) {
			setImageDrawable(placeholder)
		}
		return
	}

	Glide.with(this.context)
		.load(image)
		.placeholder(placeholder)
		.centerCrop()
		.diskCacheStrategy(DiskCacheStrategy.ALL)
		.into(this)
}

@set:BindingAdapter("activated")
var View.activated
	get() = isActivated
	set(value) {
		isActivated = value
	}