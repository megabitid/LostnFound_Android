package id.taufiq.lostandfound.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.taufiq.lostandfound.R

/**
 * Created By Taufiq on 11/11/2020.
 *
 */

class TerbaruAdapter(context: Context, listener: () -> Unit) : RecyclerView.Adapter<TerbaruAdapter.TerbaruViewHolder>() {


    class TerbaruViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun binding(context: Context, listener: ()-> Unit){}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TerbaruViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_item_category, parent, false)

        return TerbaruViewHolder(view)
    }

    override fun onBindViewHolder(holder: TerbaruViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}