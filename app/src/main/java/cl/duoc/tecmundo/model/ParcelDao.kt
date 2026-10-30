package cl.duoc.tecmundo.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// las consultas a la tabla "parcels"
@Dao
interface ParcelDao {

    // guarda una parcela nueva y devuelve su id
    @Insert
    suspend fun insert(parcel: ParcelEntity): Long

    // las parcelas de un productor; como es Flow, la lista se actualiza sola al guardar
    @Query("SELECT * FROM parcels WHERE ownerRut = :ownerRut ORDER BY id DESC")
    fun parcelsOf(ownerRut: String): Flow<List<ParcelEntity>>

    // borra la parcela con ese id (suspend: se ejecuta fuera del hilo principal)
    @Query("DELETE FROM parcels WHERE id = :id")
    suspend fun delete(id: Long)
}