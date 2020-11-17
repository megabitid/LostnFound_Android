package id.taufiq.lostandfound.data.api

import id.taufiq.lostandfound.data.remote.LoginRequest

class ApiHelper(private val apiService: ApiService) {

    suspend fun loginUser(loginRequest: LoginRequest) = apiService.loginUser(loginRequest)

    suspend fun logoutUser(token : String) = apiService.logoutUser(token)

}