package id.taufiq.lostandfound.ui.register

import android.content.ContentValues.TAG
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
import com.google.firebase.auth.FirebaseAuth
import id.taufiq.lostandfound.R
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*


@Suppress("LABEL_NAME_CLASH")
class RegisterFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
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

        mAuth = FirebaseAuth.getInstance()

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

                   if (name.isEmpty()){
                       view.text_name.error = "Nama harus diisi!"
                       view.text_name.requestFocus()
                       return@setOnClickListener
                   }

                   if (email.isEmpty()){
                       view.text_email.error = "Email harus diisi!"
                       view.text_email.requestFocus()
                       return@setOnClickListener
                   }

                   if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                       view.text_email.error = "Email tidak valid!"
                       view.text_email.requestFocus()
                       return@setOnClickListener
                   }

                   if (password.isEmpty() || password.length < 8){
                       view.text_password.error = "Password harus lebih dari 8 karakter!"
                       view.text_password.requestFocus()
                       return@setOnClickListener
                   }

                   singupUser(email, password)
               }
           }else{
               button.isEnabled = false
               button.setBackgroundResource(R.drawable.background_text_radius_before_press)
               button.setTextColor(getColor(requireContext() , R.color.white_color))
           }
        }
    }

    private fun singupUser(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}