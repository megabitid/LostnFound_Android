package id.taufiq.lostandfound.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import id.taufiq.lostandfound.data.repository.MainRepository
import id.taufiq.lostandfound.helper.Resource
import kotlinx.coroutines.Dispatchers

class SingUpGoogleViewModel(private val mainRepository: MainRepository) : ViewModel() {
    fun singUpGoogle(code : String) = liveData(
        Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.singUpGoogle(code)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}