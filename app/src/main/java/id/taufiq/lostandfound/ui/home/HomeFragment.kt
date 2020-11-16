package id.taufiq.lostandfound.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.data.remote.ApiClient
import id.taufiq.lostandfound.data.remote.LogoutResponse
import id.taufiq.lostandfound.helper.SessionManager
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAction()
    }

    private fun initAction() {
        btn_logout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        val sessionManager = SessionManager(requireContext())
        val apiClient = ApiClient()

        // Pass the token as parameter
        apiClient.getApiService().logoutUser(token = "Bearer ${sessionManager.fetchAuthToken()}")
            .enqueue(object : Callback<LogoutResponse> {
                override fun onResponse(
                    call: Call<LogoutResponse>,
                    response: Response<LogoutResponse>
                ) {
                    if (response.body()?.message.equals("The token has been blacklisted")) {
                        sessionManager.clearLoggedInToken()
                        findNavController().navigate(R.id.action_homeFragment_to_welcomeFragment)
                    } else {
                        Log.d("message", response.body()?.message.toString())
                        sessionManager.clearLoggedInToken()
                        findNavController().navigate(R.id.action_homeFragment_to_welcomeFragment)
                    }
                }

                override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                    Log.e("Error", t.message.toString())
                }

            })
    }
}