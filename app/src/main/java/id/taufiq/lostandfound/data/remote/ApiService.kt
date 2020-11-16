package id.taufiq.lostandfound.data.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


public interface ApiService {
    @FormUrlEncoded
    @POST("api/v1/android/auth/register")
    public fun createUser(
        @Field("nama") nama: String?,
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("image") image: String?
    ): Call<ResponseRegister?>?

    @POST("api/v1/android/auth/login")
    fun loginUser(
        @Body loginRequest: LoginRequest
    ):Call<ResponseLogin>
}