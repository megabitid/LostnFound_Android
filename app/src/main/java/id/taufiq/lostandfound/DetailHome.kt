package id.taufiq.lostandfound

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_detail_home.*


class DetailHome : Fragment() {

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


        Glide.with(view.context).load(args.TerbaruData.imageItem).into(id_item)
        tv_nama_item_detail.text = args.TerbaruData.itemName
        tv_date_item.text = args.TerbaruData.tanggal
        imageView4.setImageResource(args.TerbaruData.status)
        textView8.text = args.TerbaruData.kategori
        textView10.text = args.TerbaruData.merk
        textView13.text = args.TerbaruData.warna
        textView15.text = args.TerbaruData.deskripsi


        btn_claim.setOnClickListener {
            findNavController().navigate(DetailHomeDirections.actionDetailHomeToVerifikasiFragment())
        }


    }


}