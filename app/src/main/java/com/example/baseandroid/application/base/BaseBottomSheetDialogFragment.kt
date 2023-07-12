package com.example.baseandroid.application.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment<B : ViewBinding> : BottomSheetDialogFragment() {
    lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = makeBinding(inflater, container, false)
        setupView()
        return binding.root
    }

    open fun setupView() {}
}