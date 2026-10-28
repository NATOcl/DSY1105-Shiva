package cl.duoc.tecmundo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// una fila de la tabla "parcels" (cada parcela pertenece a un productor)
@Entity(tableName = "parcels")
data class ParcelEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    // el rut del productor dueno de la parcela (es unico por cuenta)
    val ownerRut: String,
    val name: String,     // nombre de la parcela
    val zone: String,     // zona referencial (texto manual, sin ubicacion precisa)
    val crop: String,     // tipo de cultivo
    val hectares: Int,     // superficie en hectareas
    val photoPath: String? = null  // ruta de la foto del terreno
)