package cl.duoc.tecmundo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// una fila de la tabla "users" (sin clave, esto es solo frontend)
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val profile: String,
    val fullName: String,
    val rut: String,
    val email: String,
    val phone: String,
    val farmName: String,
    // true mientras la sesion este abierta; cerrar sesion lo pasa a false pero la cuenta se queda
    val sessionActive: Boolean = false
)
