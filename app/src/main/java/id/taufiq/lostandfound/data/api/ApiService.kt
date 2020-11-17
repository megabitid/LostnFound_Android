package id.taufiq.lostandfound.data.api

import id.taufiq.lostandfound.data.remote.LoginRequest
import id.taufiq.lostandfound.data.remote.LoginResponse
import id.taufiq.lostandfound.data.remote.LogoutResponse
import id.taufiq.lostandfound.data.remote.RegisterResponse
import id.taufiq.lostandfound.helper.Constants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    @FormUrlEncoded
    @POST(Constants.REGISTER_URL)
    suspend fun createUser(
        @Field("nama") nama: String?,
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("image") image: String?
    ): Response<RegisterResponse>

    @POST(Constants.LOGIN_URL)
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @GET(Constants.LOGOUT_URL)
    suspend fun logoutUser(
        @Header("Authorization") token: String
    ): Response<LogoutResponse>
}