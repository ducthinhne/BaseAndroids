package com.example.baseandroid.application.base.tabbar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.baseandroid.R
import com.example.baseandroid.databinding.TabbarLayoutBinding

interface TabBar {
    var didSelectItemAt: ((Int) -> Unit)?
    var currentSelectedIndex: Int
    var view: View
}

class TabbarView(
    context: Context,
    attrs: AttributeSet
) : LinearLayout(context, attrs), TabBar {
    var binding: TabbarLayoutBinding

    private var selectedColor: Int = context.getColor(R.color.white)
    private var defaultColor: Int = context.getColor(R.color.white)

    override var currentSelectedIndex: Int = 0
        set(value) {
            selectItemAt(value)
            didSelectItemAt?.let { it1 -> it1(value) }
            field = value
        }
    override var view: View = this

    override var didSelectItemAt: ((Int) -> Unit)? = null

    private var listTabItem = mutableListOf<TabbarItemView>()

    init {

        val customAttributesStyle =
            context.obtainStyledAttributes(attrs, R.styleable.TabbarView, 0, 0)
        try {
            selectedColor = customAttributesStyle.getColor(
                R.styleable.TabbarView_tabSelectedColor,
                Color.WHITE
            )
            defaultColor =
                customAttributesStyle.getColor(R.styleable.TabbarView_defaultColor, Color.WHITE)
        } finally {
            customAttributesStyle.recycle()
        }

        binding = TabbarLayoutBinding.inflate(LayoutInflater.from(context), this, true)
        listTabItem.add(binding.tab1)
        listTabItem.add(binding.tab2)
        listTabItem.add(binding.tab3)
        listTabItem.add(binding.tab4)
        listTabItem.add(binding.tab5)
        currentSelectedIndex = 0
        listTabItem.forEachIndexed { index, tabbarItemView ->
            tabbarItemView.setOnClickListener { currentSelectedIndex = index }
        }
    }

    private fun selectItemAt(index: Int) {
        listTabItem.forEachIndexed { index1, view -> view.setSelectedView(if (index1 == index) selectedColor else defaultColor) }
    }
}