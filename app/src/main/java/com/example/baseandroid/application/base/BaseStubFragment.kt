package com.example.baseandroid.application.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.example.baseandroid.databinding.FragmentStubBinding

abstract class BaseViewStubFragment<OB : ViewBinding, V : BaseViewModel> :
    BaseVMFragment<FragmentStubBinding, V>() {
    private var hasInflated = false

    lateinit var originBinding: OB

    protected abstract fun onCreateViewAfterViewStubInflated(
        inflatedView: View
    )

    @LayoutRes
    protected abstract fun getViewStubLayoutResource(): Int

    @SuppressLint("SupportAnnotationUsage")
    @LayoutRes
    protected abstract fun makeOriginViewBinding(originView: View)

    @CallSuper
    protected fun afterViewStubInflated(originalViewContainerWithViewStub: View?) {
        hasInflated = true
    }

    override fun onResume() {
        super.onResume()
        if (!hasInflated) {
            binding.stub.layoutResource = getViewStubLayoutResource()
            if (binding.stub.parent != null) {
                val inflatedView = binding.stub.inflate()
                makeOriginViewBinding(inflatedView)
                onCreateViewAfterViewStubInflated(inflatedView)
            }
            afterViewStubInflated(view)
        }
    }

    override fun onDetach() {
        super.onDetach()
        hasInflated = false
    }
}
