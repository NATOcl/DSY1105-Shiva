package cl.duoc.tecmundo.model

// opciones fijas que el prestador puede elegir; el matching las va a cruzar con las de la solicitud
object ProviderCatalog {
    val services = listOf("Fumigación con dron", "Aplicación de fertilizante", "Monitoreo aéreo", "Siembra con dron")
    val products = listOf("Granulado", "Líquido")
    val schedules = listOf("Diurna", "Nocturna")
}

// el perfil demostrativo del prestador (aplicador): lo que ofrece y donde puede trabajar
data class ProviderProfile(
    val ownerRut: String,          // rut del aplicador dueño del perfil
    val services: Set<String>,     // servicios que ofrece
    val products: Set<String>,     // tipos de producto que sabe aplicar
    val schedules: Set<String>,    // horarios en que trabaja (diurna / nocturna)
    val crops: String,             // cultivos en los que tiene experiencia, separados por coma
    val coverageZones: String,     // zonas que cubre, separadas por coma (referenciales)
    val capacityHaPerDay: Int,     // hectareas que alcanza a aplicar por dia
    val experienceYears: Int,      // años de experiencia
    val available: Boolean         // true si hoy acepta nuevas solicitudes
)
