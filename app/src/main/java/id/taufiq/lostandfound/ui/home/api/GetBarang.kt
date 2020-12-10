package id.taufiq.lostandfound.ui.home.api

import id.taufiq.lostandfound.helper.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created By Taufiq on 12/10/2020.
 *
 */
class GetBarang {

    companion object {

        private val buildClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(object : Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + Constant.authorization)
                        .build()
                    return chain.proceed(request)
                }

            })
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()


        private fun buildRetrofit(): Retrofit {
            return Retrofit.Builder()
                .client(buildClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.BASE_URL)
                .build()
        }

        fun buildApiService(): RequestBarang =
            buildRetrofit().create(RequestBarang::class.java)

    }
}
