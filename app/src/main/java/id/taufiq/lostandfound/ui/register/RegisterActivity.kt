package id.taufiq.lostandfound.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.ui.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_back.setOnClickListener {
            Intent(this, WelcomeActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}