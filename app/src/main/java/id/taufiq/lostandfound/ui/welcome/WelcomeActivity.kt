package id.taufiq.lostandfound.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.ui.login.LoginActivity
import id.taufiq.lostandfound.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.activity_welcome.btn_singin_with_phone

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        btn_login.setOnClickListener {
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
        }

        btn_singin_with_phone.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}