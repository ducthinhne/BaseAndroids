package com.example.baseandroid.application.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.AnimBuilder
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.viewbinding.ViewBinding
import com.example.baseandroid.R
import com.example.baseandroid.application.base.tabbar.TabBar
import com.example.baseandroid.resource.customView.ProgressView
import com.example.baseandroid.resource.utils.setComplete
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {
    val activityScope: CoroutineLauncher by lazy {
        return@lazy CoroutineLauncher()
    }

    var progress: ProgressView? = null

    open val binding: B by lazy { makeBinding(layoutInflater) }

    open var tabBar: TabBar? = null
    val navContainer: NavController? by lazy {
        (navHostId?.let {
            supportFragmentManager.findFragmentById(
                it
            )
        } as? NavHostFragment)?.navController
    }

    @IdRes
    open var rootDes: Int? = null

    @IdRes
    open var navHostId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView(savedInstanceState)
    }

    open fun setupView(savedInstanceState: Bundle?) {}

    fun showTabBar(isShow: Boolean) {
        tabBar?.view?.animate()
            ?.setDuration(0)?.translationY(if (isShow) 0f else 200f)
            ?.alpha(if (isShow) 1f else 0.0f)
            ?.setComplete {
                tabBar?.view?.visibility = if (isShow) View.VISIBLE else View.GONE
            }?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancelCoroutines()
    }
}

abstract class BaseVMActivity<B : ViewBinding, VM : BaseViewModel> : BaseActivity<B>() {
    abstract val viewModel: VM

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        viewModel.isShowProgress.observe(this) { isShow ->
            showProgress(isShow)
        }
    }

    private fun showProgress(isShow: Boolean) {
        if (isShow) {
            if (progress == null) {
                progress = ProgressView()
            }
            if (progress?.isVisible == true) {
                return
            }
            progress?.show(supportFragmentManager, "")
        } else {
            progress?.dismiss()
            progress = null
        }
    }
}

fun BaseActivity<*>.pushTo(
    @IdRes resId: Int,
    args: Bundle? = null,
    anim: PushType = PushType.SLIDE
) {
    navContainer?.currentDestination?.getAction(resId)?.navOptions?.let {
        navContainer?.navigate(
            resId,
            args,
            navOptions {
                anim {
                    enter = anim.anim.enter
                    exit = anim.anim.exit
                    popEnter = anim.anim.popEnter
                    popExit = anim.anim.popExit
                }
                popUpTo(it.popUpToId) {
                    inclusive = it.isPopUpToInclusive()
                }
            }
        )
    }
}

fun BaseActivity<*>.popTo(@IdRes destinationId: Int? = null, inclusive: Boolean = false) {
    navContainer?.apply {
        if (destinationId == null) popBackStack()
        else popBackStack(destinationId, inclusive)
    }
}

fun BaseActivity<*>.popToRoot() {
    rootDes?.let { popTo(it, false) }
}

enum class PushType(val anim: AnimBuilder) {
    NONE(AnimBuilder().apply {}),
    SLIDE(
        AnimBuilder().apply {
            enter = R.anim.enter_from_right
            exit = R.anim.exit_to_left
            popEnter = R.anim.enter_from_left
            popExit = R.anim.exit_to_right
        }
    ),
    FADE(AnimBuilder().apply {
        enter = R.anim.fade_in
        exit = R.anim.fade_out
    })
}

fun <B : ViewBinding> Any.makeBinding(
    layoutInflater: LayoutInflater,
    indexGeneric: Int = 0
): B {
    val type = javaClass.genericSuperclass
    val clazz = (type as ParameterizedType).actualTypeArguments[indexGeneric] as Class<B>
    val method = clazz.getMethod(
        "inflate",
        LayoutInflater::class.java
    )

    return method.invoke(null, layoutInflater) as B
}

fun <B : ViewBinding> Any.makeBinding(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    attachToRoot: Boolean = false,
    indexGeneric: Int = 0
): B {
    val type = javaClass.genericSuperclass
    val clazz = (type as ParameterizedType).actualTypeArguments[indexGeneric] as Class<B>
    val method = clazz.getMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    )

    return method.invoke(null, layoutInflater, viewGroup, attachToRoot) as B
}
