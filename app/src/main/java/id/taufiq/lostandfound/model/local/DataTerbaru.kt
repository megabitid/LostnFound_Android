package id.taufiq.lostandfound.model.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created By Taufiq on 11/11/2020.
 *
 */
@Parcelize
data class DataTerbaru(
    val imageItem: Int,
    val tanggal: String,
    val itemName: String,
    val status: Int,
    val kategori: String,
    val merk: String,
    val warna: String,
    val deskripsi: String
) : Parcelable {
}