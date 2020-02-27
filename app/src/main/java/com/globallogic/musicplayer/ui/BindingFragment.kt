package com.globallogic.musicplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import java.lang.IllegalStateException

abstract class BindingFragment<T : ViewDataBinding> : Fragment() {
	var binding: T? = null
		private set

	abstract fun onCreateBinding(inflater: LayoutInflater, container: ViewGroup?): T

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val localBinding = onCreateBinding(inflater, container)
		localBinding.lifecycleOwner = this
		binding = localBinding

		return localBinding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val localBinding = binding
		if (localBinding != null) {
			onBindingCreated(localBinding, savedInstanceState)
		} else {
			throw IllegalStateException("Binding not created")
		}
	}

	open fun onBindingCreated(binding: T, savedInstanceState: Bundle?) {
	}

	override fun onDestroyView() {
		super.onDestroyView()

		val localBinding = binding
		if (localBinding != null) {
			onDestroyBinding()
		} else {
			throw IllegalStateException("Binding not destroyed")
		}
	}

	open fun onDestroyBinding() {
		binding = null
	}
}