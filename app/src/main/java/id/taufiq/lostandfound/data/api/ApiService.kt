package id.taufiq.lostandfound.data.api

import id.taufiq.lostandfound.data.remote.*
import id.taufiq.lostandfound.helper.Constants.USER_URL
import id.taufiq.lostandfound.helper.Constants.LOGIN_URL
import id.taufiq.lostandfound.helper.Constants.LOGOUT_URL
import id.taufiq.lostandfound.helper.Constants.REGISTER_URL
import id.taufiq.lostandfound.helper.Constants.LOGIN_GOOGLE_URL
import retrofit2.Response
import retrofit2.http.*

/**
 * Created By Gogxi on 17/11/2020.
 *
 */
interface ApiService {
    @FormUrlEncoded
    @POST(REGISTER_URL)
    suspend fun createUser(
        @Field("nama") nama: String?,
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("image") image: String?
    ): Response<UserResponse>

    @POST(LOGIN_URL)
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): Response<UserResponse>

    @GET(LOGIN_GOOGLE_URL)
    suspend fun loginGoogle(
        @Query("code") code: String?,
    ): Response<UserResponse>

    @GET(LOGOUT_URL)
    suspend fun logoutUser(
        @Header("Authorization") token: String
    ): Response<LogoutResponse>

    @GET(USER_URL)
    suspend fun getDetailUser(
        @Header("Authorization") token: String,
        @Path("id") id : Int
    ): Response<UserResponse>

    @PUT(USER_URL)
    suspend fun updateDetailUser(
        @Header("Authorization") token: String,
        @Path("id") id : Int,
        @Body userRequest: UserRequest
    ): Response<UserResponse>
}