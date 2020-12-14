package id.taufiq.lostandfound.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.adapter.CategoryAdapter
import id.taufiq.lostandfound.adapter.TerbaruAdapter
import id.taufiq.lostandfound.model.local.DataCategory
import id.taufiq.lostandfound.presenter.BarangTerbaruPresenter
import id.taufiq.lostandfound.presenter.BarangTerbaruView
import id.taufiq.lostandfound.ui.home.bbaru.Data
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), BarangTerbaruView {
    private lateinit var mAuth: FirebaseAuth
    private val data = ArrayList<DataCategory>()

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


        val presenter = BarangTerbaruPresenter(this)
        presenter.getBarangTerbaru()

        rv_terbaru.layoutManager = GridLayoutManager(context, 2)


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

    override fun getBarangSuccess(data: List<Data>) {

        rv_terbaru.adapter = context?.let {
            TerbaruAdapter(it, data) { barang ->
                val todetail = HomeFragmentDirections.actionHomeFragmentToDetailHome(barang.id)
                findNavController().navigate(todetail)
            }
        }
    }

    override fun getBarangFailure(message: String) {
        Log.d("Main Activity", "getBarangFailure: $message")
    }


}