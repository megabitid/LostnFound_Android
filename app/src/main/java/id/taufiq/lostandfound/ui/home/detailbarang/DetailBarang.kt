package id.taufiq.lostandfound.ui.home.detailbarang


import com.google.gson.annotations.SerializedName

data class DetailBarang(
    @SerializedName("barangimages")
    val barangimages: List<Barangimage>,
    @SerializedName("created_at")
    val createdAt: Any,
    @SerializedName("deskripsi")
    val deskripsi: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("kategori")
    val kategori: Kategori,
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
    @SerializedName("stasiun_id")
    val stasiunId: Int,
    @SerializedName("status_id")
    val statusId: Int,
    @SerializedName("tanggal")
    val tanggal: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("warna")
    val warna: String
)