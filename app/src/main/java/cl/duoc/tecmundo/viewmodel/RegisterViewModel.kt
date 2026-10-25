package cl.duoc.tecmundo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.tecmundo.model.AppDatabase
import cl.duoc.tecmundo.repository.UserRepository
import cl.duoc.tecmundo.model.ProfileType
import cl.duoc.tecmundo.model.UserAccount
import cl.duoc.tecmundo.model.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// los campos del formulario, para saber cual ya fue tocado
enum class Field { NAME, RUT, EMAIL, PHONE, FARM, PASSWORD }

// todo lo que la pantalla necesita mostrar, junto en un solo lugar
data class RegisterUiState(
    val profile: ProfileType = ProfileType.PRODUCTOR,
    val fullName: String = "",
    val rut: String = "",
    val email: String = "",
    val phone: String = "",
    val farmName: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val termsAccepted: Boolean = false,
    val touched: Set<Field> = emptySet(),
    val emailTaken: Boolean = false,
    val rutTaken: Boolean = false,
    val registrationDone: Boolean = false
) {
    // los errores solo salen cuando el campo ya fue tocado o se intento enviar
    val nameError get() = if (Field.NAME in touched) Validators.nameError(fullName) else null
    val rutError get() = if (Field.RUT in touched) {
        Validators.rutError(rut) ?: if (rutTaken) "Este RUT ya está registrado" else null
    } else null
    val emailError get() = if (Field.EMAIL in touched) {
        Validators.emailError(email) ?: if (emailTaken) "Este correo ya está registrado" else null
    } else null
    val phoneError get() = if (Field.PHONE in touched) Validators.phoneError(phone) else null
    val farmError get() = if (Field.FARM in touched) Validators.farmError(farmName) else null
    val passwordError get() = if (Field.PASSWORD in touched) Validators.passwordError(password) else null

    // true solo si todos los campos pasan su validacion
    val isFormValid: Boolean
        get() = Validators.nameError(fullName) == null &&
                Validators.rutError(rut) == null &&
                Validators.emailError(email) == null &&
                Validators.phoneError(phone) == null &&
                Validators.farmError(farmName) == null &&
                Validators.passwordError(password) == null

    // el checkbox de terminos es lo que habilita el boton de registro
    val submitEnabled: Boolean get() = termsAccepted
}

// maneja la logica del formulario: recibe lo escrito, valida y guarda
class RegisterViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = UserRepository(AppDatabase.get(app).userDao())

    // estado interno, solo el viewmodel lo puede cambiar
    private val _state = MutableStateFlow(RegisterUiState())

    // version de solo lectura que observa la pantalla
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    // cambios de cada campo: aca se limpia lo que escribe la persona
    fun onProfileChange(profile: ProfileType) = _state.update { it.copy(profile = profile) }

    // nombre: se descartan numeros y simbolos al escribir
    fun onNameChange(input: String) = _state.update {
        it.copy(fullName = input.filter { char -> char.isLetter() || char == ' ' }.take(60))
    }

    // rut: mientras escribe solo quedan numeros y K (sin puntos ni guion, asi el teclado no cambia);
    // el formato con puntos y guion se pone al salir del campo
    fun onRutChange(input: String) = _state.update {
        it.copy(rut = Validators.cleanRut(input).take(9), rutTaken = false)
    }

    // correo: sin espacios
    fun onEmailChange(input: String) = _state.update {
        it.copy(email = input.replace(" ", "").take(80), emailTaken = false)
    }

    // telefono: solo numeros, + y espacios
    fun onPhoneChange(input: String) = _state.update {
        it.copy(phone = input.filter { char -> char.isDigit() || char == '+' || char == ' ' }.take(15))
    }

    // finca y clave: solo se limita el largo
    fun onFarmChange(input: String) = _state.update { it.copy(farmName = input.take(60)) }
    fun onPasswordChange(input: String) = _state.update { it.copy(password = input.take(64)) }

    // el ojito para mostrar u ocultar la clave
    fun togglePasswordVisible() = _state.update { it.copy(passwordVisible = !it.passwordVisible) }

    // marcar o desmarcar los terminos
    fun onTermsChange(isAccepted: Boolean) = _state.update { it.copy(termsAccepted = isAccepted) }

    // cuando sale de un campo lo marcamos como tocado para mostrar su error
    // y si es el rut, aprovechamos de ordenarlo con puntos y guion
    fun onFieldBlur(field: Field) = _state.update {
        val rut = if (field == Field.RUT) Validators.formatRut(it.rut) else it.rut
        it.copy(touched = it.touched + field, rut = rut)
    }

    // al presionar crear mi cuenta: valida y, si todo esta bien, guarda
    fun onSubmit() {
        val currentState = _state.value
        // sin aceptar los terminos no hace nada
        if (!currentState.submitEnabled) return
        // si algo esta mal, mostramos todos los errores juntos
        if (!currentState.isFormValid) {
            _state.update { it.copy(touched = Field.entries.toSet()) }
            return
        }
        viewModelScope.launch {
            // no se puede repetir el correo ni el rut de otra cuenta
            val rut = Validators.formatRut(currentState.rut)
            val emailTaken = repo.emailExists(currentState.email)
            val rutTaken = repo.rutExists(rut)
            if (emailTaken || rutTaken) {
                _state.update {
                    it.copy(emailTaken = emailTaken, rutTaken = rutTaken,
                        touched = it.touched + Field.EMAIL + Field.RUT)
                }
                return@launch
            }
            // guardamos los datos sin espacios sobrantes (la clave se guarda cifrada)
            repo.saveAccount(
                UserAccount(
                    profile = currentState.profile,
                    fullName = currentState.fullName.trim(),
                    rut = rut,
                    email = currentState.email.trim(),
                    phone = currentState.phone.trim(),
                    farmName = currentState.farmName.trim()
                ),
                currentState.password
            )
            // listo: avisamos que se registro para mostrar el dialogo de exito
            _state.update { it.copy(registrationDone = true) }
        }
    }

    // se llama cuando la persona cierra el dialogo de exito
    // y se deja el formulario vacio por si quiere crear otra cuenta
    fun onRegistrationHandled() {
        _state.value = RegisterUiState()
    }
}