package id.taufiq.lostandfound.model.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created By Taufiq on 11/9/2020.
 *
 */

@Parcelize
data class DataCategory(val viewType: Int, val title: String, val image: Int? = null) : Parcelable
