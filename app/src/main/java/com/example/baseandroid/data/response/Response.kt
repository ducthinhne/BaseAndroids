package com.example.baseandroid.data.response

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("message")
    var message: String?,

    @SerializedName("status_code")
    var status_code: Int?,
)