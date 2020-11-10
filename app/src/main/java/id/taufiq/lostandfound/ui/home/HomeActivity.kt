package id.taufiq.lostandfound.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.adapter.CategoryAdapter
import id.taufiq.lostandfound.helper.showToast
import id.taufiq.lostandfound.model.local.DataCategory
import id.taufiq.lostandfound.ui.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private val data = ArrayList<DataCategory>()
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mAuth = FirebaseAuth.getInstance()

        listOfData()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        rv_category.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_category.adapter = CategoryAdapter(this,data){
            showToast("clicked ${it.title}")
        }

        btn_logout.setOnClickListener {
            mAuth.signOut()
            Intent(this, WelcomeActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }


    private fun listOfData() {
        data.add(
            DataCategory(
                CategoryAdapter.VIEW_TYPE_ONE, "Elektronik", R.drawable.image_elektronic
            )
        )
        data.add(
            DataCategory(
                CategoryAdapter.VIEW_TYPE_ONE, "Tas & Dompet", R.drawable.image_tas
            )
        )
        data.add(
            DataCategory(CategoryAdapter.VIEW_TYPE_ONE, "Perhiasan", R.drawable.image_perhiasan),
        )

        data.add(
            DataCategory(CategoryAdapter.VIEW_TYPE_TWO, "Lainnya", R.drawable.ic_category_icon),
        )


    }

}