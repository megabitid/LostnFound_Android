package id.taufiq.lostandfound.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.model.local.DataTerbaru
import kotlinx.android.synthetic.main.row_item_terbaru.view.*

/**
 * Created By Taufiq on 11/11/2020.
 *
 */

class TerbaruAdapter(
    val context: Context,
    val data: List<DataTerbaru>,
    val listener: (DataTerbaru) -> Unit,
) : RecyclerView.Adapter<TerbaruAdapter.TerbaruViewHolder>() {


    class TerbaruViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun binding(data: DataTerbaru, context: Context, listener: (DataTerbaru) -> Unit) {
            with(itemView) {
                tv_item.text = data.itemName
                tv_tanggal.text = data.tanggal
                iv_item.setImageResource(data.imageItem)
                iv_status.setImageResource(data.status)

                setOnClickListener { listener(data) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TerbaruViewHolder {
        return TerbaruViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_terbaru, parent, false))
    }

    override fun onBindViewHolder(holder: TerbaruViewHolder, position: Int) {
        holder.binding(data[position], context, listener)
    }

    override fun getItemCount(): Int = data.size


}