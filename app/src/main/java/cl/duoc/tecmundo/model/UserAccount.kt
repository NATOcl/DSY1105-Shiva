package cl.duoc.tecmundo.model

import android.provider.ContactsContract

// los datos de la cuenta que guardamos localmente

data class UserAccount(
    val profile: ProfileType, // productor o aplicador
    val fullName: String, // nombre completo
    val rut: String, // rut
    val email: String, // email
    val phone: String, // telefono
    val farmName: String //nombre campo - parcela

)