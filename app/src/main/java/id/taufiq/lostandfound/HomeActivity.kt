package id.taufiq.lostandfound

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import id.taufiq.lostandfound.adapter.CategoryAdapter
import id.taufiq.lostandfound.adapter.TerbaruAdapter
import id.taufiq.lostandfound.helper.showToast
import id.taufiq.lostandfound.model.local.DataCategory
import id.taufiq.lostandfound.model.local.DataTerbaru
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private val data = ArrayList<DataCategory>()
    private val dataTerbaru = ArrayList<DataTerbaru>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // dummy data
        dataCategory()
        dataTerbaru()

        initListCategory()
        initListTerbaru()
    }

    private fun initListCategory() {
        rv_category.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_category.adapter = CategoryAdapter(this,data){
            showToast("clicked ${it.title}")
        }
    }



    private fun initListTerbaru() {
        rv_terbaru.layoutManager = GridLayoutManager(this,2)
        rv_terbaru.adapter = TerbaruAdapter(this,dataTerbaru){

        }
    }


    private fun dataCategory() {
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


        data.subList(0,3)


    }

    private fun dataTerbaru() {
        dataTerbaru.add(
            DataTerbaru(
                R.drawable.tas,"6 November 2020", "Tas Orange",R.drawable.ic_image_hilang,
                "Barang","Gucci","Orange",resources.getString(R.string.lorem_ipsum)
            )
        )


        dataTerbaru.add(
            DataTerbaru(
                R.drawable.hp,"7 November 2020", "Hp Iphone",R.drawable.ic_image_ketemu,
                "Elektronik","Iphone","Hitam",resources.getString(R.string.lorem_ipsum)
            )
        )


        dataTerbaru.add(
            DataTerbaru(
                R.drawable.tas,"6 November 2020", "Tas Orange",R.drawable.ic_image_hilang,
                "Barang","Gucci","Orange",resources.getString(R.string.lorem_ipsum)
            )
        )


        dataTerbaru.add(
            DataTerbaru(
                R.drawable.hp,"7 November 2020", "Hp Iphone",R.drawable.ic_image_ketemu,
                "Elektronik","Iphone","Hitam",resources.getString(R.string.lorem_ipsum)
            )
        )



    }

}