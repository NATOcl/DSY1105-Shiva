package cl.duoc.tecmundo.repository

import cl.duoc.tecmundo.model.ProviderProfile
import cl.duoc.tecmundo.model.ProviderProfileDao
import cl.duoc.tecmundo.model.ProviderProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// guarda y lee el perfil del prestador desde Room
class ProviderProfileRepository(private val dao: ProviderProfileDao) {

    private val separator = "|"

    // el perfil de un aplicador, o null si todavia no lo completa
    fun profileOf(ownerRut: String): Flow<ProviderProfile?> =
        dao.profileOf(ownerRut).map { it?.toProfile() }

    // guarda (o actualiza) el perfil
    suspend fun save(profile: ProviderProfile) = dao.upsert(profile.toEntity())

    private fun String.toSet(): Set<String> =
        split(separator).filter { it.isNotBlank() }.toSet()

    private fun ProviderProfile.toEntity() = ProviderProfileEntity(
        ownerRut = ownerRut,
        services = services.joinToString(separator),
        products = products.joinToString(separator),
        schedules = schedules.joinToString(separator),
        crops = crops.trim(),
        coverageZones = coverageZones.trim(),
        capacityHaPerDay = capacityHaPerDay,
        experienceYears = experienceYears,
        available = available
    )

    private fun ProviderProfileEntity.toProfile() = ProviderProfile(
        ownerRut = ownerRut,
        services = services.toSet(),
        products = products.toSet(),
        schedules = schedules.toSet(),
        crops = crops,
        coverageZones = coverageZones,
        capacityHaPerDay = capacityHaPerDay,
        experienceYears = experienceYears,
        available = available
    )
}
