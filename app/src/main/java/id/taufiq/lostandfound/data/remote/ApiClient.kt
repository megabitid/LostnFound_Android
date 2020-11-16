package id.taufiq.lostandfound.data.remote

import id.taufiq.lostandfound.helper.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient {
//    fun getClientInstance(): Retrofit {
//        val gson = GsonBuilder().setLenient().create()
//        return Retrofit.Builder()
//            .baseUrl("https://megabit-lostnfound.herokuapp.com/")
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
//    }

    private lateinit var apiService: ApiService

    fun getApiService(): ApiService {
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiService = retrofit.create(ApiService::class.java)
        }

        return apiService
    }
}