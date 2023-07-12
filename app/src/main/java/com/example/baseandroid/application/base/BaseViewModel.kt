package com.example.baseandroid.application.base

import androidx.lifecycle.ViewModel
import com.example.baseandroid.resource.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    val isShowProgress = SingleLiveEvent<Boolean>()

    fun showProgress() {
        if (isShowProgress.value == true) {
            return
        }
        isShowProgress.postValue(true)
    }

    fun hideProgress() {
        if (isShowProgress.value == false) {
            return
        }
        isShowProgress.postValue(false)
    }
}