package id.taufiq.lostandfound.ui.akun

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import id.taufiq.lostandfound.data.remote.UserRequest
import id.taufiq.lostandfound.data.repository.MainRepository
import id.taufiq.lostandfound.helper.Resource
import kotlinx.coroutines.Dispatchers

/**
 * Created By Gogxi on 1/12/2020.
 *
 */

class UpdateDetailViewModel(private val mainRepository: MainRepository) : ViewModel() {
    fun updateDetailUser(token : String, id : Int, userRequest : UserRequest) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.updateDetailUser(token, id, userRequest)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}