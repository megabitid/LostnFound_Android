package id.taufiq.lostandfound.data.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By Gogxi on 17/11/2020.
 *
 */

data class UserRequest (
    @SerializedName("nama")
    @Expose
    var nama: String? = null,

    @SerializedName("email")
    @Expose
    var email: String? = null,

    @SerializedName("password")
    @Expose
    var password: String? = null,

    @SerializedName("image")
    @Expose
    var image: String? = null,
)