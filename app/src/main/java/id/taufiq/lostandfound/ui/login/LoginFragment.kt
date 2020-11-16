package id.taufiq.lostandfound.ui.login

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.data.remote.ApiClient
import id.taufiq.lostandfound.data.remote.LoginRequest
import id.taufiq.lostandfound.data.remote.LoginResponse
import id.taufiq.lostandfound.helper.SessionManager
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAction()
    }

    private fun initAction(){
        btn_back.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
        }

        btn_registration.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        btn_login.setOnClickListener {
            val email = view?.text_email?.editText?.text.toString().trim()
            val password = view?.text_password?.editText?.text.toString().trim()

            if (email.isEmpty()){
                view?.text_email?.error = "Email harus diisi!"
                view?.text_email?.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                view?.text_email?.error = "Email tidak valid!"
                view?.text_email?.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 8){
                view?.text_password?.error = "Password tidak valid!"
                view?.text_password?.requestFocus()
                return@setOnClickListener
            }

            loginUser(email, password)
        }
    }

    private fun loginUser(email : String, password : String){
        val request = LoginRequest()
        request.email = email
        request.password = password

        val sessionManager = SessionManager(requireContext())
        val apiClient = ApiClient()

        apiClient.getApiService().loginUser(request).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                try {
                    val user = response.body()
                    if (user != null) {
                        if(user.email != null)
                            Log.d("email", user.email.toString())
                            Log.d("token", user.token.toString())
                            Toast.makeText(context , "Login Berhasil.", Toast.LENGTH_SHORT).show()
                            sessionManager.saveAuthToken(user.token.toString())
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    } else {
                        Toast.makeText(context , "Email atau Password salah!", Toast.LENGTH_SHORT).show()
                    }
                } catch (exception: Exception){
                    Log.e("exception", exception.message.toString())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Error", t.message.toString())
                Toast.makeText(context , "Login Gagal.", Toast.LENGTH_SHORT).show()
            }

        })
    }
}