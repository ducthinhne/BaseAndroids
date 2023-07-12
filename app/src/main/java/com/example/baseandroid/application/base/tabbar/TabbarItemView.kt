package com.example.baseandroid.application.base.tabbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.baseandroid.R
import com.example.baseandroid.databinding.TabbarItemLayoutBinding

class TabbarItemView : ConstraintLayout {
    constructor(context: Context) : super(context) {
        initView(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(attrs, 0)
    }

    constructor(
        context: Context, attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initView(attrs, defStyleAttr)
    }

    lateinit var binding: TabbarItemLayoutBinding

    private fun initView(attrs: AttributeSet?, @AttrRes defStyleAttr: Int) {
        binding = TabbarItemLayoutBinding.inflate(LayoutInflater.from(context), this, true)
        val customAttributesStyle =
            context.obtainStyledAttributes(attrs, R.styleable.TabbarItemView, defStyleAttr, 0)

        try {
            val icon = customAttributesStyle.getDrawable(R.styleable.TabbarItemView_myIcon)
            val title = customAttributesStyle.getString(R.styleable.TabbarItemView_myTitle)
            binding.titleLabel.text = title
            binding.imageView.setImageDrawable(icon)
        } finally {
            customAttributesStyle.recycle()
        }
    }

    fun setSelectedView(tintColor: Int) {
        binding.titleLabel.setTextColor(tintColor)
        binding.imageView.setColorFilter(tintColor)
    }
}