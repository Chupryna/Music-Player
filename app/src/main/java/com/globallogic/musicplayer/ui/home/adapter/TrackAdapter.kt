package com.globallogic.musicplayer.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private val layoutInflater: LayoutInflater) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val dataBinding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        return TrackViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    class TrackViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}