package cl.duoc.tecmundo.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// Aquí guardamos lo que el usuario va escribiendo, nada más
class RegisterFormState {
    var fullName by mutableStateOf("")
    var email by mutableStateOf("")
    var farmName by mutableStateOf("")
    var password by mutableStateOf("")

    // Controla si se ve o no la contraseña
    var passwordVisible by mutableStateOf(false)

    // Marca si aceptó los términos
    var termsAccepted by mutableStateOf(false)

    // El botón solo se activa cuando aceptó los términos
    val canRegister: Boolean get() = termsAccepted
}