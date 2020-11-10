package id.taufiq.lostandfound.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.ui.home.HomeActivity
import id.taufiq.lostandfound.ui.register.RegisterActivity
import id.taufiq.lostandfound.ui.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        btn_back.setOnClickListener {
            Intent(this, WelcomeActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        btn_registration.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
//                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
                finish()
            }
        }

        btn_login.setOnClickListener {
            val email = text_email.editText?.text.toString().trim()
            val password = text_password.editText?.text.toString().trim()

            if (email.isEmpty()){
                text_email.error = "Email harus diisi!"
                text_email.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                text_email.error = "Email tidak valid!"
                text_email.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 8){
                text_password.error = "Password harus lebih dari 8 karakter!"
                text_password.requestFocus()
                return@setOnClickListener
            }

            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    Intent(this, HomeActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                        finish()
                    }
                } else{
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val user = mAuth.currentUser
        if(user != null){
            Intent(this, HomeActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }
}