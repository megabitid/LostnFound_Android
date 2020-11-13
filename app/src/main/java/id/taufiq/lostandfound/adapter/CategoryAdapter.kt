package id.taufiq.lostandfound.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.taufiq.lostandfound.R
import id.taufiq.lostandfound.model.local.DataCategory
import kotlinx.android.synthetic.main.row_item_category.view.*
import kotlinx.android.synthetic.main.row_item_category.view.iv_category
import kotlinx.android.synthetic.main.row_item_category2.view.*


/**
 * Created By Taufiq on 11/10/2020.
 *
 */
class CategoryAdapter(
    private val context: Context,
    private val data: List<DataCategory>,
    private val listener: (DataCategory) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }


    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun binding(context: Context, data: DataCategory, listener: (DataCategory) -> Unit) {
            with(itemView) {
                tv_category.text = data.title
                data.image?.let { iv_category.setImageResource(it) }
                setOnClickListener { listener(data) }
            }

        }
    }

    class CategoryViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun binding(context: Context, data: DataCategory, listener: (DataCategory) -> Unit) {
            with(itemView) {
                setOnClickListener { listener(data) }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ONE) {
            CategoryViewHolder(
                LayoutInflater.from(context).inflate(R.layout.row_item_category, parent, false)
            )
        } else {
            CategoryViewHolder1(
                LayoutInflater.from(context).inflate(R.layout.row_item_category2, parent, false)
            )
        }

    }


    override fun getItemCount(): Int = data.size


    override fun getItemViewType(position: Int): Int {
        return data[position].viewType
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (data[position].viewType == VIEW_TYPE_ONE) {
            (holder as CategoryViewHolder).binding(context, data[position], listener)
        } else {
            (holder as CategoryViewHolder1).binding(context, data[position], listener)
        }
    }

}

