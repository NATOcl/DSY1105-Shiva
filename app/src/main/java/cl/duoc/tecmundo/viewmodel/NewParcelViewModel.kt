package cl.duoc.tecmundo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.tecmundo.model.AppDatabase
import cl.duoc.tecmundo.model.Validators
import cl.duoc.tecmundo.repository.ParcelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// los campos del formulario de parcela (para marcar cuales ya se tocaron)
enum class ParcelField { NAME, ZONE, CROP, HECTARES }

// todo lo que la pantalla de nueva parcela necesita mostrar
data class NewParcelUiState(
    val name: String = "",
    val zone: String = "",
    val crop: String = "",
    val hectares: String = "",      // se guarda como texto mientras se escribe
    val photoPath: String? = null,  // ruta de la foto del terreno
    val touched: Set<ParcelField> = emptySet(),
    val saving: Boolean = false,
    val saved: Boolean = false      // true cuando la parcela ya quedo guardada
) {
    // los errores solo salen cuando el campo ya fue tocado o se intento guardar
    val nameError get() = if (ParcelField.NAME in touched) Validators.parcelNameError(name) else null
    val zoneError get() = if (ParcelField.ZONE in touched) Validators.zoneError(zone) else null
    val cropError get() = if (ParcelField.CROP in touched) Validators.cropError(crop) else null
    val hectaresError get() = if (ParcelField.HECTARES in touched) Validators.hectaresError(hectares) else null

    // el formulario esta listo cuando los cuatro campos pasan su validacion
    val isFormValid: Boolean get() =
        Validators.parcelNameError(name) == null &&
                Validators.zoneError(zone) == null &&
                Validators.cropError(crop) == null &&
                Validators.hectaresError(hectares) == null
}

// maneja el formulario de nueva parcela: valida lo escrito y lo guarda en la base de datos
class NewParcelViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = ParcelRepository(AppDatabase.get(app).parcelDao())

    private val _state = MutableStateFlow(NewParcelUiState())
    val state: StateFlow<NewParcelUiState> = _state.asStateFlow()

    // nombre y zona: se limita el largo
    fun onNameChange(input: String) = _state.update { it.copy(name = input.take(40)) }
    fun onZoneChange(input: String) = _state.update { it.copy(zone = input.take(40)) }

    // cultivo: se limita el largo
    fun onCropChange(input: String) = _state.update { it.copy(crop = input.take(30)) }

    // superficie: solo deja pasar numeros (maximo 5 digitos)
    fun onHectaresChange(input: String) = _state.update { it.copy(hectares = input.filter { c -> c.isDigit() }.take(5)) }

    // guarda la ruta de la foto que sacó el usuario
    fun onPhotoChange(path: String?) = _state.update { it.copy(photoPath = path) }

    // cuando sale de un campo lo marcamos como tocado para mostrar su error
    fun onFieldBlur(field: ParcelField) = _state.update { it.copy(touched = it.touched + field) }

    // al presionar guardar: valida y, si todo esta bien, guarda la parcela del productor (por su rut)
    fun onSubmit(ownerRut: String) {
        val current = _state.value
        if (current.saving) return
        if (!current.isFormValid) {
            // si falta algo, marcamos todos los campos para que salgan los errores
            _state.update { it.copy(touched = ParcelField.entries.toSet()) }
            return
        }
        _state.update { it.copy(saving = true) }
        viewModelScope.launch {
            repo.saveParcel(
                ownerRut = ownerRut,
                name = current.name,
                zone = current.zone,
                crop = current.crop,
                hectares = current.hectares.toInt(),
                photoPath = current.photoPath
            )
            _state.update { it.copy(saving = false, saved = true) }
        }
    }

    // deja el formulario vacio (al salir de la pantalla)
    fun reset() {
        _state.value = NewParcelUiState()
    }
}