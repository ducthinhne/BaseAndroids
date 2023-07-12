package com.example.baseandroid.application.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import androidx.viewbinding.ViewBinding

typealias MyFragment = BaseFragment<*>
typealias MyActivity = BaseActivity<*>

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    val fragmentScope: CoroutineLauncher by lazy {
        return@lazy CoroutineLauncher()
    }

    val permissionsResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.entries.all { it.value }) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }

    val binding: B
        get() = _binding ?: error("Could not find binding")

    private var _binding: B? = null

    var isVisibleTabbar: Boolean = false

    var shouldReloadView: Boolean = false

    val mActivity: MyActivity?
        get() = this.activity as? MyActivity

    open fun setupView() {}

    open fun makeViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (shouldReloadView) {
            _binding = makeBinding(inflater, container, false)
            setupView()
            return binding.root
        }
        if (_binding == null) {
            _binding = makeBinding(inflater, container, false)
            setupView()
        }

        return this.binding.root
    }

    open fun onPermissionGranted() {}
    open fun onPermissionDenied() {}

    override fun onDestroy() {
        super.onDestroy()
        fragmentScope.cancelCoroutines()
        _binding = null
    }
}

abstract class BaseVMFragment<B : ViewBinding, V : BaseViewModel> : BaseFragment<B>() {
    abstract val viewModel: V
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.isShowProgress.observe(this) { isShow ->
            (mActivity as? BaseVMActivity<*, *>)?.viewModel?.isShowProgress?.postValue(isShow)
        }
    }
}

fun MyFragment.pushTo(@IdRes resId: Int, args: Bundle? = null, anim: PushType = PushType.SLIDE) {
    mActivity?.pushTo(resId, args, anim)
}

fun MyFragment.popTo(@IdRes destinationId: Int? = null, inclusive: Boolean = false) {
    mActivity?.popTo(destinationId, inclusive)
}

fun MyFragment.popToRoot() {
    mActivity?.popToRoot()
}