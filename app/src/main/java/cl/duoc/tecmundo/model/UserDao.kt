package cl.duoc.tecmundo.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// las consultas a la tabla "users"
@Dao
interface UserDao {

    // guarda una cuenta nueva y devuelve su id
    @Insert
    suspend fun insert(user: UserEntity): Long

    // cuantas cuentas hay; como es Flow, avisa solo cuando cambia
    @Query("SELECT COUNT(*) FROM users")
    fun countUsers(): Flow<Int>

    // la cuenta con la sesion abierta (null si no hay ninguna)
    @Query("SELECT * FROM users WHERE sessionActive = 1 LIMIT 1")
    fun activeUser(): Flow<UserEntity?>

    // busca una cuenta por correo, sin importar mayusculas
    @Query("SELECT * FROM users WHERE email = :email COLLATE NOCASE LIMIT 1")
    suspend fun findByEmail(email: String): UserEntity?

    // abre la sesion de ese usuario y deja cerradas las demas (en una sola instruccion)
    @Query("UPDATE users SET sessionActive = (id = :id)")
    suspend fun openSession(id: Long)

    // cierra la sesion; los datos de la cuenta no se borran
    @Query("UPDATE users SET sessionActive = 0")
    suspend fun closeSession()
}
