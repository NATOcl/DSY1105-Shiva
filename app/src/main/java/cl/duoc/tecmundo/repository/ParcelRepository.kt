package cl.duoc.tecmundo.repository

import cl.duoc.tecmundo.model.Parcel
import cl.duoc.tecmundo.model.ParcelDao
import cl.duoc.tecmundo.model.ParcelEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// guarda y lee las parcelas desde la base de datos Room
class ParcelRepository(private val dao: ParcelDao) {

    // las parcelas de un productor; se actualiza sola cuando se agrega una nueva
    fun parcelsOf(ownerRut: String): Flow<List<Parcel>> =
        dao.parcelsOf(ownerRut).map { list -> list.map { it.toParcel() } }

    // guarda una parcela nueva para ese productor
    suspend fun saveParcel(ownerRut: String, name: String, zone: String, crop: String, hectares: Int, photoPath: String?) {
        dao.insert(
            ParcelEntity(
                ownerRut = ownerRut,
                name = name.trim(),
                zone = zone.trim(),
                crop = crop.trim(),
                hectares = hectares,
                photoPath = photoPath
            )
        )
    }

    // pasa la fila de la base de datos al modelo que usan las pantallas
    private fun ParcelEntity.toParcel() = Parcel(
        id = id,
        name = name,
        crop = crop,
        hectares = hectares,
        zone = zone,
        photoPath = photoPath
    )
}