package id.taufiq.lostandfound.data.api

import id.taufiq.lostandfound.helper.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created By Gogxi on 17/11/2020.
 *
 */
class ApiClient {
    private lateinit var apiService: ApiService

    fun getApiService(): ApiService {
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiService = retrofit.create(ApiService::class.java)
        }

        return apiService
    }
}