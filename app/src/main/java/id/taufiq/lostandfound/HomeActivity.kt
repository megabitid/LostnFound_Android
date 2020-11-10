package id.taufiq.lostandfound

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import id.taufiq.lostandfound.adapter.CategoryAdapter
import id.taufiq.lostandfound.helper.showToast
import id.taufiq.lostandfound.model.local.DataCategory
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private val data = ArrayList<DataCategory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        listOfData()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        rv_category.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_category.adapter = CategoryAdapter(this,data){
            showToast("clicked ${it.title}")
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
            DataCategory(CategoryAdapter.VIEW_TYPE_TWO, "Lainnya",R.drawable.ic_category_icon),
        )


    }

}