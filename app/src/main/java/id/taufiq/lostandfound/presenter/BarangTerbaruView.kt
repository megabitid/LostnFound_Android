package id.taufiq.lostandfound.presenter

import id.taufiq.lostandfound.ui.home.data.Data

/**
 * Created By Taufiq on 12/10/2020.
 *
 */
interface BarangTerbaruView {

    fun getBarangSuccess(data: List<Data>)
    fun getBarangFailure(message: String)
    fun getImageBarangTerbaru()
}