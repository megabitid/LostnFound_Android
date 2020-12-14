package id.taufiq.lostandfound.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.ui.home.bbaru.Data
import kotlinx.android.synthetic.main.row_item_terbaru.view.*

/**
 * Created By Taufiq on 11/11/2020.
 *
 */

class TerbaruAdapter(
    val context: Context,
    private val data: List<Data>,
    private val listener: (Data) -> Unit,
) : RecyclerView.Adapter<TerbaruAdapter.TerbaruViewHolder>() {


    class TerbaruViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun binding(position: Int, data: Data, listener: (Data) -> Unit) {
            with(itemView) {
                tv_item.text = data.namaBarang
                tv_tanggal.text = data.tanggal
                Glide.with(context).apply {
                    load(data.barangimages[position].uri).into(iv_item)
                }
                // TODO add status

                setOnClickListener { listener(data) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TerbaruViewHolder {
        return TerbaruViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item_terbaru, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TerbaruViewHolder, position: Int) {
        holder.binding(position, data[position], listener)
    }

    override fun getItemCount(): Int = data.size


}