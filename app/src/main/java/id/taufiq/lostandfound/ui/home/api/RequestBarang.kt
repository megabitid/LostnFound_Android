package id.taufiq.lostandfound.ui.home.api

import id.taufiq.lostandfound.ui.home.bbaru.ListBarang
import id.taufiq.lostandfound.ui.home.detailbarang.DetailBarang
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created By Taufiq on 12/10/2020.
 *
 */
interface RequestBarang {
    @GET("api/v2/barang")
    suspend fun getBarang(): Response<ListBarang>

    @GET("api/v2/barang/{id}")
    suspend fun getDetailBarang(@Path("id") id: Int): Response<DetailBarang>


}