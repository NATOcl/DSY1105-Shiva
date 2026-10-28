package cl.duoc.tecmundo.model

// una parcela que registra el productor (se guarda en room, en la tabla "parcels")
data class Parcel(
    val id: Long,         // identificador unico, lo usa la lista para no repetir tarjetas
    val name: String,     // nombre del campo
    val crop: String,     // cultivo (maiz, trigo, etc)
    val hectares: Int,    // superficie en hectareas
    val zone: String = "", // zona referencial (texto manual, sin ubicacion precisa)
    val photoPath: String? = null // ruta de la foto del terreno
)