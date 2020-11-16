package id.taufiq.lostandfound.data.remote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient {

    fun getClientInstance(): Retrofit{
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl("https://megabit-lostnfound.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}