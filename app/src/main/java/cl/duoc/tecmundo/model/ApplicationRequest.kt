package cl.duoc.tecmundo.model

import com.google.gson.annotations.SerializedName

data class ApplicationRequest(
    val id: Int = 0,
    @SerializedName("farm_name") val farmName: String,
    @SerializedName("distance_km") val distanceKm: Int = 10,
    val status: String = "PENDIENTE",
    val product: String,
    val hectares: Int,
    val schedule: String,
    val imageRes: Int? = null,
    @SerializedName("evidence_image") val imagePath: String? = null,
    @SerializedName("has_water") val hasWater: Boolean = true,
    val notes: String = "",
    @SerializedName("parcel_id") val parcelId: Long = 0
)