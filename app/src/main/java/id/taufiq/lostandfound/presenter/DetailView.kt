package id.taufiq.lostandfound.presenter

import id.taufiq.lostandfound.ui.home.detailbarang.DetailBarang

/**
 * Created By Taufiq on 12/14/2020.
 *
 */
interface DetailView {
    fun getDataDetailSucces(detailBarang: DetailBarang)
    fun getDataDetailFailed(message: String)
}