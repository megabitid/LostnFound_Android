package id.taufiq.lostandfound.presenter

import id.taufiq.lostandfound.ui.home.api.GetBarang
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created By Taufiq on 12/14/2020.
 *
 */
class DetailPresenter(val detailView: DetailView) {
    fun getDetailById(id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = GetBarang.buildApiService().getDetailBarang(id)
            if (response.isSuccessful){
                response.body()?.let { detailView.getDataDetailSucces(it) }
            }

            detailView.getDataDetailFailed(response.message())
        }
    }
}