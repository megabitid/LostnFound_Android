package id.taufiq.lostandfound.ui.register

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.data.remote.*
import id.taufiq.lostandfound.helper.Constants
import id.taufiq.lostandfound.helper.SessionManager
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("LABEL_NAME_CLASH")
class RegisterFragment : Fragment() {
    private lateinit var checkBox: CheckBox
    private lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button = view.findViewById(R.id.btn_create_account)
        checkBox = view.findViewById(R.id.checkbox1)

        btn_back.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_welcomeFragment)
        }

        checkBox.setOnClickListener {
           if (checkBox.isChecked){
               button.isEnabled = true
               button.setBackgroundResource(R.drawable.background_text_radius_gradient)
               button.setTextColor(getColor(requireContext() , R.color.grey_color))
               btn_create_account.setOnClickListener {
                   val name = view.text_name.editText?.text.toString().trim()
                   val email = view.text_email.editText?.text.toString().trim()
                   val password = view.text_password.editText?.text.toString().trim()

                   if (name.isEmpty()) {
                       view.text_name.error = "Nama harus diisi!"
                       view.text_name.requestFocus()
                       return@setOnClickListener
                   }

                   if (email.isEmpty()) {
                       view.text_email.error = "Email harus diisi!"
                       view.text_email.requestFocus()
                       return@setOnClickListener
                   }

                   if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                       view.text_email.error = "Email tidak valid!"
                       view.text_email.requestFocus()
                       return@setOnClickListener
                   }

                   if (password.isEmpty() || password.length < 8) {
                       view.text_password.error = "Password harus lebih dari 8 karakter!"
                       view.text_password.requestFocus()
                       return@setOnClickListener
                   }

                   registerUser(name, email, password, Constants.IMG_DEFAULT)
               }
           }else{
               button.isEnabled = false
               button.setBackgroundResource(R.drawable.background_text_radius_before_press)
               button.setTextColor(getColor(requireContext() , R.color.white_color))
           }
        }
    }

    private fun registerUser(name : String, email : String, password : String, image : String){
        val sessionManager = SessionManager(requireContext())
        val apiClient = ApiClient()

        apiClient.getApiService().createUser(name, email, password, image)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    try {
                        val user = response.body()
                        if (user != null) {
                            if(user.email != null)
                                Log.d("email", user.email.toString())
                                Log.d("token", user.token.toString())
                                Toast.makeText(context , "Register Berhasil.", Toast.LENGTH_SHORT).show()
                                sessionManager.saveAuthToken(user.token.toString())
                                findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                        } else {
                            Toast.makeText(context , "Email Sudah Terdaftar", Toast.LENGTH_SHORT).show()
                        }
                    } catch (exception: Exception){
                        Log.e("exception", exception.message.toString())
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.e("Error", t.message.toString())
                    Toast.makeText(context , "Register Gagal.", Toast.LENGTH_SHORT).show()
                }

            })
    }
}