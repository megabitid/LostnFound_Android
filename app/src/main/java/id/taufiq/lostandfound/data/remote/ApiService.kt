package id.taufiq.lostandfound.data.remote

import id.taufiq.lostandfound.helper.Constants
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @FormUrlEncoded
    @POST(Constants.REGISTER_URL)
    fun createUser(
        @Field("nama") nama: String?,
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("image") image: String?
    ): Call<RegisterResponse>

    @POST(Constants.LOGIN_URL)
    fun loginUser(
        @Body loginRequest: LoginRequest
    ):Call<LoginResponse>

    @GET(Constants.LOGOUT_URL)
    fun logoutUser(
        @Header("Authorization") token: String
    ):Call<LogoutResponse>
}