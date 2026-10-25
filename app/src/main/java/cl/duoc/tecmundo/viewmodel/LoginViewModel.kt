package cl.duoc.tecmundo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.tecmundo.model.AppDatabase
import cl.duoc.tecmundo.model.LoginResult
import cl.duoc.tecmundo.model.ProfileType
import cl.duoc.tecmundo.model.Validators
import cl.duoc.tecmundo.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// todo lo que la pantalla de login necesita mostrar
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val touched: Set<Field> = emptySet(),
    val loading: Boolean = false,
    val result: LoginResult? = null
) {
    // los errores solo salen cuando el campo ya fue tocado o se intento enviar
    val emailError get() = if (Field.EMAIL in touched) Validators.emailError(email) else null
    val passwordError get() = if (Field.PASSWORD in touched && password.isEmpty()) "Ingresa tu contraseña" else null
    val isFormValid: Boolean get() = Validators.emailError(email) == null && password.isNotEmpty()
}

// maneja el formulario de login: valida lo escrito y revisa correo, clave y perfil
class LoginViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = UserRepository(AppDatabase.get(app).userDao())

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    // correo: sin espacios
    fun onEmailChange(input: String) = _state.update { it.copy(email = input.replace(" ", "").take(80)) }

    // clave: solo se limita el largo
    fun onPasswordChange(input: String) = _state.update { it.copy(password = input.take(64)) }

    // el ojito para mostrar u ocultar la clave
    fun togglePasswordVisible() = _state.update { it.copy(passwordVisible = !it.passwordVisible) }

    // cuando sale de un campo lo marcamos como tocado para mostrar su error
    fun onFieldBlur(field: Field) = _state.update { it.copy(touched = it.touched + field) }

    // al presionar iniciar sesion: valida y, si todo esta bien, revisa en la base de datos
    fun onSubmit(profile: ProfileType) {
        val current = _state.value
        if (current.loading) return
        if (!current.isFormValid) {
            _state.update { it.copy(touched = setOf(Field.EMAIL, Field.PASSWORD)) }
            return
        }
        _state.update { it.copy(loading = true) }
        viewModelScope.launch {
            val result = repo.logIn(current.email, current.password, profile)
            _state.update { it.copy(loading = false, result = result) }
        }
    }

    // se llama cuando la persona cierra el dialogo con el resultado
    fun onResultHandled() = _state.update { it.copy(result = null) }

    // deja el formulario vacio (al salir de la pantalla)
    fun reset() {
        _state.value = LoginUiState()
    }
}
