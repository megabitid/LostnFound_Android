package id.taufiq.lostandfound.ui.home.bbaru


import com.google.gson.annotations.SerializedName


data class Data(
    @SerializedName("barangimages")
    val barangimages: List<Barangimage>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("kategori_id")
    val kategoriId: Int,
    @SerializedName("lokasi")
    val lokasi: String,
    @SerializedName("merek")
    val merek: String,
    @SerializedName("nama_barang")
    val namaBarang: String,
    @SerializedName("stasiun")
    val stasiun: Stasiun,
    @SerializedName("status_id")
    val statusId: Int,
    @SerializedName("tanggal")
    val tanggal: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("warna")
    val warna: String
)