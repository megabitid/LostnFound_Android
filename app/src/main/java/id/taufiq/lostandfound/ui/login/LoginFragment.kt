package id.taufiq.lostandfound.ui.login

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.data.remote.ApiClient
import id.taufiq.lostandfound.data.remote.ApiService
import id.taufiq.lostandfound.data.remote.LoginRequest
import id.taufiq.lostandfound.data.remote.ResponseLogin
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_register.btn_back
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {
//    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        mAuth = FirebaseAuth.getInstance()

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
                view?.text_password?.error = "Password harus lebih dari 8 karakter!"
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

        val apiClient = ApiClient().getClientInstance().create(ApiService::class.java)
        apiClient.loginUser(request).enqueue(object : Callback<ResponseLogin>{
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                val user = response.body()
                Toast.makeText(context , "Login Berhasil.", Toast.LENGTH_SHORT).show()
                Log.e("email", user!!.email.toString())
                Log.e("token", user.token.toString())
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.e("Error", t.message.toString())
                Toast.makeText(context , "Login Gagal.", Toast.LENGTH_SHORT).show()
            }

        })
    }

//    private fun loginUser(email: String, password: String) {
//
//        mAuth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(requireActivity()) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
//                    Log.d(TAG, "signInWithEmail:success")
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "signInWithEmail:failure", task.exception)
//                    Toast.makeText(context , "Login failed.",
//                        Toast.LENGTH_SHORT).show()
//                }
//            }
//    }

//    override fun onStart() {
//        super.onStart()
//        val user = mAuth.currentUser
//        if(user != null){
//            mAuth.signOut()
//        }
//    }
}