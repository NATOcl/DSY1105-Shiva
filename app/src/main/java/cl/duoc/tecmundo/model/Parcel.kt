package cl.duoc.tecmundo.model

// una parcela que registra el productor (por ahora solo vive en memoria, no en room)
data class Parcel(
    val id: Int,          // identificador unico, lo usa la lista para no repetir tarjetas
    val name: String,     // nombre del campo
    val crop: String,     // cultivo (maiz, trigo, etc)
    val hectares: Int     // superficie en hectareas
)
