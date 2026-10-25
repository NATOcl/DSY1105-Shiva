package cl.duoc.tecmundo.model

// lo que pasa al intentar iniciar sesion
sealed interface LoginResult {
    // correo, clave y perfil correctos: login exitoso
    data object Success : LoginResult

    // el correo no existe o la clave no coincide
    data object InvalidCredentials : LoginResult

    // el correo y la clave estan bien, pero la cuenta no es de ese perfil
    data class NoProfile(val profile: ProfileType) : LoginResult
}
