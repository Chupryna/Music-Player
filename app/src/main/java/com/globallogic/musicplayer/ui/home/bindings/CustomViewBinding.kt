package com.globallogic.musicplayer.ui.home.bindings

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

@BindingAdapter("setImage", "placeholder", requireAll = false)
fun ImageView.bindImageView(image: ByteArray?, placeholder: Drawable?) {
	if (image == null)
		return

	Glide.with(this.context)
		.load(image)
		.placeholder(placeholder)
		.centerCrop()
		.diskCacheStrategy(DiskCacheStrategy.ALL)
		.into(this)
}