package id.taufiq.lostandfound.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.taufiq.lostandfound.data.api.ApiHelper
import id.taufiq.lostandfound.data.repository.MainRepository
import id.taufiq.lostandfound.ui.akun.DetailViewModel
import id.taufiq.lostandfound.ui.akun.LogoutViewModel
import id.taufiq.lostandfound.ui.akun.UpdateDetailViewModel
import id.taufiq.lostandfound.ui.login.LoginViewModel
import id.taufiq.lostandfound.ui.register.RegisterViewModel
import id.taufiq.lostandfound.ui.welcome.LoginGoogleViewModel

/**
 * Created By Gogxi on 17/11/2020.
 *
 */

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(MainRepository(apiHelper)) as T
        } else if (modelClass.isAssignableFrom(LogoutViewModel::class.java)){
            return LogoutViewModel(MainRepository(apiHelper)) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(MainRepository(apiHelper)) as T
        } else if (modelClass.isAssignableFrom(LoginGoogleViewModel::class.java)){
            return LoginGoogleViewModel(MainRepository(apiHelper)) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(MainRepository(apiHelper)) as T
        } else if (modelClass.isAssignableFrom(UpdateDetailViewModel::class.java)){
            return UpdateDetailViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}