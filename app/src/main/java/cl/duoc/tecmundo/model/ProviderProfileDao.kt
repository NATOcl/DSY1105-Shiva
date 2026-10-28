package cl.duoc.tecmundo.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// las consultas a la tabla "provider_profiles"
@Dao
interface ProviderProfileDao {

    // guarda el perfil; si ese aplicador ya tenia uno lo reemplaza
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: ProviderProfileEntity)

    // el perfil de un aplicador (null si todavia no lo completa); se actualiza solo al guardar
    @Query("SELECT * FROM provider_profiles WHERE ownerRut = :ownerRut LIMIT 1")
    fun profileOf(ownerRut: String): Flow<ProviderProfileEntity?>
}
