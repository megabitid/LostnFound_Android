package id.taufiq.lostandfound.data.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By Gogxi on 17/11/2020.
 *
 */
data class UserResponse(
    @SerializedName("image")
    @Expose
    val image: String? = null,

    @SerializedName("role")
    @Expose
    val role: Int? = null,

    @SerializedName("name")
    @Expose
    val nama: String? = null,

    @SerializedName("nip")
    @Expose
    val nip: Any? = null,

    @SerializedName("updatedAt")
    @Expose
    val updatedAt: String? = null,

    @SerializedName("createdAt")
    @Expose
    val createdAt: String? = null,

    @SerializedName("emailVerifiedAt")
    @Expose
    val emailVerifiedAt: Any? = null,

    @SerializedName("id")
    @Expose
    val id: Int? = null,

    @SerializedName("email")
    @Expose
    val email: String? = null,

    @SerializedName("token")
    @Expose
    val token: String? = null
)
