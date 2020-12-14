package id.taufiq.lostandfound.presenter

import id.taufiq.lostandfound.ui.home.api.GetBarang
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created By Taufiq on 12/10/2020.
 *
 */
class BarangTerbaruPresenter(val view: BarangTerbaruView) {

    fun getBarangTerbaru() {
        CoroutineScope(Dispatchers.Main).launch {
            val response = GetBarang.buildApiService().getBarang()
            if (response.isSuccessful) {
                // get respond
            } else {
                view.getBarangFailure(response.message().toString())
            }
        }
    }

}