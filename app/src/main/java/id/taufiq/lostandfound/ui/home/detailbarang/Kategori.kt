package id.taufiq.lostandfound.ui.home.detailbarang


import com.google.gson.annotations.SerializedName

data class Kategori(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nama")
    val nama: String
)