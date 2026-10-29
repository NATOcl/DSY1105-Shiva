package cl.duoc.tecmundo.model

// datos ficticios para probar el panel del administrador (no hay nombres, rut ni contactos reales)
// son los mismos que usa el documento de casos de prueba

// un usuario de ejemplo: productor o prestador
data class SimUser(val id: String, val role: String, val zone: String, val detail: String)

// el perfil de un prestador de ejemplo
data class SimProvider(
    val id: String,
    val zones: String,        // zonas que cubre
    val crops: String,        // cultivos con experiencia
    val services: String,     // servicios que ofrece
    val capacity: String,     // hectareas por dia
    val experience: String,   // años de experiencia
    val available: Boolean    // si acepta nuevas solicitudes
)

// una solicitud de servicio de ejemplo publicada por un productor
data class SimRequest(
    val id: String,
    val ownerId: String,      // productor que la publico
    val summary: String,      // servicio, cultivo y superficie
    val zone: String,
    val schedule: String,     // diurna o nocturna
    val status: String        // resultado del matching

)

object SampleData {
    val users = listOf(
        SimUser("PROD-01", "Productor", "Valle de Colchagua", "Maíz · 25 ha"),
        SimUser("PROD-02", "Productor", "Maule", "Trigo · 40 ha"),
        SimUser("PREST-01", "Prestador", "Colchagua y Maule", "Fumigación y fertilizante"),
        SimUser("PREST-02", "Prestador", "Maule", "Fertilizante"),
        SimUser("PREST-03", "Prestador", "Ñuble", "Monitoreo aéreo")
    )

    val providers = listOf(
        SimProvider(
            "PREST-01",
            "Colchagua y Maule",
            "Maíz y trigo",
            "Fumigación con dron, aplicación de fertilizante",
            "40 ha/día",
            "5 años",
            true
        ),
        SimProvider(
            "PREST-02",
            "Maule",
            "Trigo",
            "Aplicación de fertilizante",
            "25 ha/día",
            "3 años",
            true
        ),
        SimProvider("PREST-03", "Ñuble", "Vid", "Monitoreo aéreo", "60 ha/día", "8 años", false)
    )

    val requests = listOf(
        SimRequest(
            "SOL-01",
            "PROD-01",
            "Fumigación con dron · maíz · 25 ha",
            "Valle de Colchagua",
            "Diurna",
            "1 prestador compatible"
        ),
        SimRequest(
            "SOL-02",
            "PROD-02",
            "Fertilizante · trigo · 40 ha",
            "Maule",
            "Diurna",
            "2 prestadores compatibles"
        ),
        SimRequest(
            "SOL-03",
            "PROD-02",
            "Siembra con dron · trigo · 40 ha",
            "Maule",
            "Nocturna",
            "Sin prestadores compatibles"
        )
    )
}