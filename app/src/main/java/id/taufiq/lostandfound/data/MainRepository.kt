package id.taufiq.lostandfound.data

import id.taufiq.lostandfound.data.api.ApiHelper
import id.taufiq.lostandfound.data.remote.LoginRequest

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun createUser(nama : String, email: String, password: String, image: String) = apiHelper.createUser(nama, email, password, image)

    suspend fun loginUser(loginRequest: LoginRequest) = apiHelper.loginUser(loginRequest)

    suspend fun logoutUser(token : String) = apiHelper.logoutUser(token)
}