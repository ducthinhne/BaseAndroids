package com.example.baseandroid.resource.customView

import com.example.baseandroid.application.base.BaseDialogFragment
import com.example.baseandroid.databinding.CustomDialogFragmentBinding
import javax.inject.Inject

class ProgressView @Inject constructor() : BaseDialogFragment<CustomDialogFragmentBinding>() {

    override fun setupView() {
        super.setupView()
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

}