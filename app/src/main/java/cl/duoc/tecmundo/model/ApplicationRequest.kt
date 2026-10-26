package cl.duoc.tecmundo.model

// solicitud de aplicacion que publica un productor, es lo que ve el aplicador en su pantalla
data class ApplicationRequest(
    val id: Int,              // identificador unico de la solicitud
    val farmName: String,     // nombre del fundo o parcela
    val distanceKm: Int,      // distancia entre el campo y el aplicador
    val status: String,       // estado, ej: PENDIENTE
    val product: String,      // tipo de producto: granulado o liquido
    val hectares: Int,        // superficie a aplicar
    val schedule: String,     // horario de aplicacion: diurna o nocturna
    val imageRes: Int? = null // foto del campo, si es null se usa una por defecto
)