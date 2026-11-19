package cl.duoc.tecmundo.repository

import cl.duoc.tecmundo.model.ProviderProfile
import cl.duoc.tecmundo.remote.RetrofitInstance

class ProviderProfileRepository {

    suspend fun profileOf(ownerRut: String): ProviderProfile? {
        return RetrofitInstance.api.obtenerPerfilesProveedor()
            .firstOrNull { it.ownerRut == ownerRut }
    }

    suspend fun save(profile: ProviderProfile) {
        val existente = profileOf(profile.ownerRut)
        if (existente != null && existente.id > 0) {
            RetrofitInstance.api.actualizarPerfilProveedor(existente.id, profile.copy(id = existente.id))
        } else {
            RetrofitInstance.api.crearPerfilProveedor(profile)
        }
    }
}