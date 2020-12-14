package id.taufiq.lostandfound.ui.home.bbaru


import com.google.gson.annotations.SerializedName

data class ListBarang(
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("links")
    val links: Links
)