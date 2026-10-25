package cl.duoc.tecmundo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// una fila de la tabla "users" (la clave se guarda cifrada, nunca en texto plano)
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val profile: String,
    val fullName: String,
    val rut: String,
    val email: String,
    val phone: String,
    val farmName: String,
    // clave cifrada y su sal (ver PasswordHasher)
    val passwordHash: String,
    val passwordSalt: String,
    // true mientras la sesion este abierta; cerrar sesion lo pasa a false pero la cuenta se queda
    val sessionActive: Boolean = false
)
