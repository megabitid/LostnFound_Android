package id.taufiq.lostandfound.ui.login

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.data.api.ApiClient
import id.taufiq.lostandfound.data.api.ApiHelper
import id.taufiq.lostandfound.data.remote.LoginRequest
import id.taufiq.lostandfound.helper.SessionManager
import id.taufiq.lostandfound.helper.Status
import id.taufiq.lostandfound.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_register.btn_back

class LoginFragment : Fragment() {
    private lateinit var viewModel: LoginViewModel

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
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(ApiClient().getApiService()))
        ).get(LoginViewModel::class.java)
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

            setupObservers(email, password)
        }
    }

    private fun setupObservers(email : String, password : String) {
        val request = LoginRequest()
        request.email = email
        request.password = password

        viewModel.loginUser(request).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        if (resource.data?.isSuccessful!!){
                            val sessionManager = SessionManager(requireContext())
                            sessionManager.saveAuthToken(resource.data.body()?.token.toString())
                            resource.data.body()?.id?.let { it1 -> sessionManager.saveUserId(it1) }
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                            Log.d("test", resource.data.body().toString())
                            Toast.makeText(context , "Login Berhasil.", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.e("test", resource.data.body().toString())
                            Toast.makeText(context , "Email atau Password salah!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        Log.e("error", it.message.toString())
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }
}