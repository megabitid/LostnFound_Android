package id.taufiq.lostandfound.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.adapter.CategoryAdapter
import id.taufiq.lostandfound.adapter.TerbaruAdapter
import id.taufiq.lostandfound.helper.showToast
import id.taufiq.lostandfound.model.local.DataCategory
import id.taufiq.lostandfound.model.local.DataTerbaru
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private val data = ArrayList<DataCategory>()
    private val dataTerbaru = ArrayList<DataTerbaru>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        listOfData()
        initRecyclerView()
        dataTerbaru()
        initListTerbaru()


        // Nav host fragment
        val host: NavHostFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.container) as NavHostFragment?
            ?: return
        // nav controller
        val navController = host.navController

        // Setup bottom navigation view
        bottom_nav_id?.setupWithNavController(navController)

    }


    private fun initRecyclerView() {
        rv_category.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_category.adapter = context?.let {
            CategoryAdapter(it, data) {
                context?.showToast("clicked ${it.title}")
            }
        }
    }

    private fun initListTerbaru() {
        rv_terbaru.layoutManager = GridLayoutManager(context, 2)
        rv_terbaru.adapter = context?.let {
            TerbaruAdapter(it, dataTerbaru) {

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

        data.subList(0, 4)

    }


    private fun dataTerbaru() {
        dataTerbaru.add(
            DataTerbaru(
                R.drawable.tas, "6 November 2020", "Tas Orange", R.drawable.ic_image_hilang,
                "Barang", "Gucci", "Orange", resources.getString(R.string.lorem_ipsum)
            )
        )


        dataTerbaru.add(
            DataTerbaru(
                R.drawable.hp, "7 November 2020", "Hp Iphone", R.drawable.ic_image_ketemu,
                "Elektronik", "Iphone", "Hitam", resources.getString(R.string.lorem_ipsum)
            )
        )


        dataTerbaru.add(
            DataTerbaru(
                R.drawable.tas, "6 November 2020", "Tas Orange", R.drawable.ic_image_hilang,
                "Barang", "Gucci", "Orange", resources.getString(R.string.lorem_ipsum)
            )
        )


        dataTerbaru.add(
            DataTerbaru(
                R.drawable.hp, "7 November 2020", "Hp Iphone", R.drawable.ic_image_ketemu,
                "Elektronik", "Iphone", "Hitam", resources.getString(R.string.lorem_ipsum)
            )
        )


    }


}