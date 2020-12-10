package id.taufiq.lostandfound.ui.home.api

import id.taufiq.lostandfound.helper.Constant
import id.taufiq.lostandfound.ui.home.data.BarangTerbaru
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * Created By Taufiq on 12/10/2020.
 *
 */
interface RequestBarang {
    @GET("api/v2/barang")
    suspend fun getBarang(): Response<BarangTerbaru>

//  Get Image
//    @GET()
//    suspend fun getImageBarang(): Response<>
}