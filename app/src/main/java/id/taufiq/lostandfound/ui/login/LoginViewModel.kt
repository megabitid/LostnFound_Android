package id.taufiq.lostandfound.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import id.taufiq.lostandfound.data.remote.LoginRequest
import id.taufiq.lostandfound.data.repository.MainRepository
import id.taufiq.lostandfound.helper.Resource
import kotlinx.coroutines.Dispatchers


/**
 * Created By Gogxi on 17/11/2020.
 *
 */
class LoginViewModel (private val mainRepository: MainRepository) : ViewModel() {
    fun loginUser(loginRequest: LoginRequest) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.loginUser(loginRequest)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}