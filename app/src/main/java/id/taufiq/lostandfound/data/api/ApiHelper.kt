package id.taufiq.lostandfound.data.api

import id.taufiq.lostandfound.data.remote.LoginRequest

/**
 * Created By Gogxi on 17/11/2020.
 *
 */
class ApiHelper (private val apiService: ApiService) {

    suspend fun createUser(nama : String, email: String, password: String, image: String) = apiService.createUser(nama, email, password, image)

    suspend fun loginGoogle(code : String) = apiService.loginGoogle(code)

    suspend fun loginUser(loginRequest: LoginRequest) = apiService.loginUser(loginRequest)

    suspend fun logoutUser(token : String) = apiService.logoutUser(token)

}