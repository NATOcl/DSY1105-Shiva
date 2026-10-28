package cl.duoc.tecmundo.repository

import cl.duoc.tecmundo.model.Parcel
import cl.duoc.tecmundo.model.ParcelDao
import cl.duoc.tecmundo.model.ParcelEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// guarda y lee las parcelas desde Room
class ParcelRepository(private val dao: ParcelDao) {

    // las parcelas de un productor, listas para mostrar
    fun parcelsOf(ownerRut: String): Flow<List<Parcel>> =
        dao.parcelsOf(ownerRut).map { list ->
            list.map { Parcel(it.id, it.name, it.crop, it.hectares, it.zone) }
        }

    // guarda una parcela nueva
    suspend fun save(ownerRut: String, name: String, crop: String, hectares: Int, zone: String) {
        dao.insert(
            ParcelEntity(
                ownerRut = ownerRut, name = name.trim(), crop = crop.trim(),
                hectares = hectares, zone = zone.trim()
            )
        )
    }
}