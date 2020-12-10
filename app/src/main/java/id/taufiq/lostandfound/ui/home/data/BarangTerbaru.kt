package id.taufiq.lostandfound.ui.home.data


import com.google.gson.annotations.SerializedName

data class BarangTerbaru(
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("links")
    val links: Links,
    @SerializedName("meta")
    val meta: Meta
)