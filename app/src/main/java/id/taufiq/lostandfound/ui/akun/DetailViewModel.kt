package id.taufiq.lostandfound.ui.akun

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import id.taufiq.lostandfound.data.repository.MainRepository
import id.taufiq.lostandfound.helper.Resource
import kotlinx.coroutines.Dispatchers

/**
 * Created By Gogxi on 1/12/2020.
 *
 */

class DetailViewModel(private val mainRepository: MainRepository) : ViewModel() {
    fun getDetailUser(token : String, id : Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getDetailUser(token, id)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}