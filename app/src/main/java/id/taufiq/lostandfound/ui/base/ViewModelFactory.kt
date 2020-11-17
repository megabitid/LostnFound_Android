package id.taufiq.lostandfound.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.taufiq.lostandfound.data.MainRepository
import id.taufiq.lostandfound.data.api.ApiHelper
import id.taufiq.lostandfound.ui.home.LogoutViewModel
import id.taufiq.lostandfound.ui.login.LoginViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(MainRepository(apiHelper)) as T
        } else if (modelClass.isAssignableFrom(LogoutViewModel::class.java)){
            return LogoutViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}

