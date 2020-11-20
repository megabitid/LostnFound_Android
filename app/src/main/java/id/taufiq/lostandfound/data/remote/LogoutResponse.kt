package id.taufiq.lostandfound.data.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By Gogxi on 17/11/2020.
 *
 */

data class LogoutResponse(
    @SerializedName("message")
    @Expose
    val message: String? = null
)