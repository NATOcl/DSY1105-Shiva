package cl.duoc.tecmundo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// una fila de la tabla "parcels"
@Entity(tableName = "parcels")
data class ParcelEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ownerRut: String,   // rut del productor dueño de la parcela
    val name: String,
    val crop: String,
    val hectares: Int,
    val zone: String,
    val photoPath: String? = null,   // ruta de la foto del terreno
    val hasWater: Boolean = true,       // si el terreno dispone de agua
    val obstacles: String = ""          // obstáculos del terreno (opcional)
)