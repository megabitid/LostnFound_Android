package id.taufiq.lostandfound

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import id.taufiq.lostandfound.helper.openActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setGradientText()
    }

    /*
    * set gradient to custom textview
    * */
    fun setGradientText(){
        tv_lihat_semua.setColorGradient(R.color.orange_1,R.color.orange_2)
        tv_lihat_semua2.setColorGradient(R.color.orange_1,R.color.orange_2)
    }
}