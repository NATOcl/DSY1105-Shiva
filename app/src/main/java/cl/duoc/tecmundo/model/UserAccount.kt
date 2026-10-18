package cl.duoc.tecmundo.model

// datos de la cuenta que guardamos en el telefono (sin clave, esto es solo frontend)
data class UserAccount(
    val profile: ProfileType,
    val fullName: String,
    val rut: String,
    val email: String,
    val phone: String,
    val farmName: String
)
