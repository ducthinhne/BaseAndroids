package com.example.baseandroid.application.base.tabbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.baseandroid.application.base.BaseFragment
import com.example.baseandroid.databinding.CollectionDemoBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TabbarFragment @Inject constructor(private val fragments: List<Fragment>) : BaseFragment<CollectionDemoBinding>() {

    private lateinit var collectionAdapter: CollectionPagerAdapter

    val currentSelectedPosition: Int
        get() = binding.pager.currentItem

    override fun setupView() {
        super.setupView()
        collectionAdapter = CollectionPagerAdapter(this)
        binding.pager.isSaveEnabled = false
        binding.pager.isUserInputEnabled = false
        binding.pager.offscreenPageLimit = fragments.size
        binding.pager.adapter = collectionAdapter
        collectionAdapter.setUpFragment(fragments = fragments)
        binding.pager.setCurrentItem(0, false)
    }

    fun setCurrentItem(index: Int, scrollEnable: Boolean = true) {
        binding.pager.setCurrentItem(index, scrollEnable)
    }

    fun getFragment(index: Int): Fragment {
        return fragments[index]
    }
}