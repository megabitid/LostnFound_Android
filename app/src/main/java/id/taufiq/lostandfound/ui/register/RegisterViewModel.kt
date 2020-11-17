package id.taufiq.lostandfound.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import id.taufiq.lostandfound.data.repository.MainRepository
import id.taufiq.lostandfound.helper.Resource
import kotlinx.coroutines.Dispatchers


/**
 * Created By Gogxi on 17/11/2020.
 *
 */
class RegisterViewModel(private val mainRepository: MainRepository) : ViewModel() {
    fun createUser(nama : String, email: String, password: String, image: String) = liveData(
        Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.createUser(nama, email, password, image)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}