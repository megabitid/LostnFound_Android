package id.taufiq.lostandfound.ui.home.detailbarang


import com.google.gson.annotations.SerializedName

data class Barangimage(
    @SerializedName("barang_id")
    val barangId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("nama")
    val nama: String,
    @SerializedName("uri")
    val uri: String
)