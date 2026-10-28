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

enum class ParcelField { NAME, CROP, HECTARES, ZONE }

// todo lo que el formulario de nueva parcela necesita mostrar
data class NewParcelUiState(
    val name: String = "",
    val crop: String = "",
    val hectares: String = "",
    val zone: String = "",
    val photoPath: String? = null,
    val hasWater: Boolean = true,
    val obstacles: String = "",
    val touched: Set<ParcelField> = emptySet()
) {
    // los errores solo salen cuando el campo ya fue tocado
    val nameError get() = if (ParcelField.NAME in touched) Validators.parcelNameError(name) else null
    val cropError get() = if (ParcelField.CROP in touched) Validators.cropError(crop) else null
    val hectaresError get() = if (ParcelField.HECTARES in touched) Validators.hectaresError(hectares) else null
    val zoneError get() = if (ParcelField.ZONE in touched) Validators.zoneError(zone) else null

    val isValid: Boolean
        get() = Validators.parcelNameError(name) == null &&
                Validators.cropError(crop) == null &&
                Validators.hectaresError(hectares) == null &&
                Validators.zoneError(zone) == null
}

class NewParcelViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = ParcelRepository(AppDatabase.get(app).parcelDao())

    private val _state = MutableStateFlow(NewParcelUiState())
    val state: StateFlow<NewParcelUiState> = _state.asStateFlow()

    fun onNameChange(v: String) = _state.update { it.copy(name = v) }
    fun onCropChange(v: String) = _state.update { it.copy(crop = v) }
    // la superficie solo acepta digitos
    fun onHectaresChange(v: String) = _state.update { it.copy(hectares = v.filter(Char::isDigit).take(5)) }
    fun onZoneChange(v: String) = _state.update { it.copy(zone = v) }

    fun onPhotoChange(path: String?) = _state.update { it.copy(photoPath = path) }

    fun onWaterChange(v: Boolean) = _state.update { it.copy(hasWater = v) }

    fun onObstaclesChange(v: String) = _state.update { it.copy(obstacles = v.take(80)) }

    fun onFieldBlur(field: ParcelField) = _state.update { it.copy(touched = it.touched + field) }

    // valida todo; si esta bien guarda la parcela y avisa a la pantalla
    fun onSubmit(ownerRut: String, onSaved: () -> Unit) {
        _state.update { it.copy(touched = ParcelField.entries.toSet()) }
        val s = _state.value
        if (!s.isValid) return
        viewModelScope.launch {
            repo.save(ownerRut, s.name, s.crop, s.hectares.toInt(), s.zone, s.photoPath,  s.hasWater, s.obstacles)
            onSaved()
        }
    }
}