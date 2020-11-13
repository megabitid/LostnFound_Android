package id.taufiq.lostandfound.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.ui.home.HomeActivity
import id.taufiq.lostandfound.ui.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

//        btn_back.setOnClickListener {
//            Intent(this, WelcomeActivity::class.java).also {
//                startActivity(it)
//            }
//        }
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