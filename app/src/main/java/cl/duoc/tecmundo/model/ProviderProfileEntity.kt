package cl.duoc.tecmundo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// una fila de la tabla "provider_profiles" (un perfil por aplicador, la clave es su rut)
// las listas se guardan como texto separado por "|" para no complicar la base de datos
@Entity(tableName = "provider_profiles")
data class ProviderProfileEntity(
    @PrimaryKey val ownerRut: String,
    val services: String,
    val products: String,
    val schedules: String,
    val crops: String,
    val coverageZones: String,
    val capacityHaPerDay: Int,
    val experienceYears: Int,
    val available: Boolean
)
