package cl.duoc.tecmundo.model

import com.google.gson.annotations.SerializedName

object ProviderCatalog {
    val services = listOf("Fumigación con dron", "Aplicación de fertilizante", "Monitoreo aéreo", "Siembra con dron")
    val products = listOf("Granulado", "Líquido")
    val schedules = listOf("Diurna", "Nocturna")
}

data class ProviderProfile(
    @SerializedName("owner_rut") val ownerRut: String,
    val services: Set<String>,
    val products: Set<String>,
    val schedules: Set<String>,
    val crops: String,
    @SerializedName("coverage_zones") val coverageZones: String,
    @SerializedName("capacity_ha_per_day") val capacityHaPerDay: Int,
    @SerializedName("experience_years") val experienceYears: Int,
    val available: Boolean,
    val id: Long = 0
)