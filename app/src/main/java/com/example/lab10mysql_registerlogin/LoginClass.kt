package com.example.lab10mysql_registerlogin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginClass(
    @Expose
    @SerializedName("success") val success: Int,

    @Expose
    @SerializedName("std_id") val std_id: String,

    @Expose
    @SerializedName("role") val role: String
)
