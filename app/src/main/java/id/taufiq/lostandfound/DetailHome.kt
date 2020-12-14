package id.taufiq.lostandfound

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import id.taufiq.lostandfound.presenter.DetailPresenter
import id.taufiq.lostandfound.presenter.DetailView
import id.taufiq.lostandfound.ui.home.detailbarang.DetailBarang
import kotlinx.android.synthetic.main.fragment_detail_home.*


class DetailHome : Fragment(), DetailView {

    private val args by navArgs<DetailHomeArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val presenter = DetailPresenter(this)
        presenter.getDetailById(args.id)



        btn_claim.setOnClickListener {
            findNavController().navigate(DetailHomeDirections.actionDetailHomeToVerifikasiFragment())
        }


    }

    override fun getDataDetailSucces(detailBarang: DetailBarang) {
        detailBarang.run {
            tv_nama_item_detail.text = namaBarang
            tv_date_item.text = tanggal
            tv_desc.text = deskripsi
            tv_warna.text = warna
            tv_merek.text = merek
            tv_stasiun_loc.text = stasiun.nama
            tv_category_detail.text = kategori.nama


        }


    }

    override fun getDataDetailFailed(message: String) {
        Log.d("Detail Activity", "getDataDetailFailed: $message ")
    }


}