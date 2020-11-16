package id.taufiq.lostandfound.data.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("message")
    @Expose
    val message: String? = null
)