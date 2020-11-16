package id.taufiq.lostandfound.data.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterRequest (
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