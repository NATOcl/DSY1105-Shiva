package cl.duoc.tecmundo.repository

import cl.duoc.tecmundo.model.Parcel
import cl.duoc.tecmundo.remote.RetrofitInstance

class ParcelRepository {

    suspend fun parcelsOf(ownerRut: String): List<Parcel> {
        return RetrofitInstance.api.obtenerParcelas()
            .filter { it.ownerRut == ownerRut }
    }

    suspend fun save(
        ownerRut: String, name: String, crop: String, hectares: Int, zone: String,
        photoPath: String?, hasWater: Boolean, obstacles: String
    ) {
        val nueva = Parcel(
            ownerRut = ownerRut,
            name = name.trim(),
            crop = crop.trim(),
            hectares = hectares,
            zone = zone.trim(),
            photoPath = photoPath,
            hasWater = hasWater,
            obstacles = obstacles.trim()
        )
        RetrofitInstance.api.crearParcela(nueva)
    }

    suspend fun delete(id: Long) {
        RetrofitInstance.api.eliminarParcela(id)
    }
}