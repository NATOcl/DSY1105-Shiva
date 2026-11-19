package cl.duoc.tecmundo.model

import com.google.gson.annotations.SerializedName

data class Parcel(
    val id: Long = 0,
    val name: String,
    val crop: String,
    val hectares: Int,
    val zone: String = "",
    @SerializedName("photo_path") val photoPath: String? = null,
    @SerializedName("has_water") val hasWater: Boolean = true,
    val obstacles: String = "",
    @SerializedName("owner_rut") val ownerRut: String = ""
)