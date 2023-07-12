package com.example.baseandroid.view.empty

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.baseandroid.R
import com.example.baseandroid.application.base.BaseVMFragment
import com.example.baseandroid.application.base.BaseViewModel
import com.example.baseandroid.application.base.pushTo
import com.example.baseandroid.databinding.FragmentEmptyBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class EmptyFragment : BaseVMFragment<FragmentEmptyBinding, EmptyFragmentViewModel>() {

    override val viewModel: EmptyFragmentViewModel by viewModels()

    override fun setupView() {
        super.setupView()
        binding.root.setBackgroundColor(Color.GRAY)
        binding.root.setOnClickListener {
            pushTo(R.id.action_emptyFragment_to_emptyFragment2)
        }
    }
}

@AndroidEntryPoint
class EmptyFragment2 : BaseVMFragment<FragmentEmptyBinding, EmptyFragmentViewModel>() {

    override val viewModel: EmptyFragmentViewModel by viewModels()

    override fun setupView() {
        super.setupView()
        binding.root.setBackgroundColor(Color.GREEN)
        binding.root.setOnClickListener {
            pushTo(R.id.emptyFragment)
        }
    }
}

@HiltViewModel
class EmptyFragmentViewModel @Inject constructor() : BaseViewModel()