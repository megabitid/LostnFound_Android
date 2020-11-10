package id.taufiq.lostandfound.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.ui.register.RegisterActivity
import id.taufiq.lostandfound.ui.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_back.setOnClickListener {
            Intent(this, WelcomeActivity::class.java).also {
                startActivity(it)
            }
        }

        btn_registration.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}