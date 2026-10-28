package cl.duoc.tecmundo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.tecmundo.model.AppDatabase
import cl.duoc.tecmundo.model.ProviderProfile
import cl.duoc.tecmundo.model.Validators
import cl.duoc.tecmundo.repository.ProviderProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ProviderField { SERVICES, PRODUCTS, SCHEDULES, CROPS, COVERAGE, CAPACITY, EXPERIENCE }

// todo lo que el formulario del perfil del prestador necesita mostrar
data class ProviderProfileUiState(
    val services: Set<String> = emptySet(),
    val products: Set<String> = emptySet(),
    val schedules: Set<String> = emptySet(),
    val crops: String = "",
    val coverage: String = "",
    val capacity: String = "",
    val experience: String = "",
    val available: Boolean = true,
    val touched: Set<ProviderField> = emptySet()
) {
    // los errores solo salen cuando el campo ya fue tocado
    val servicesError get() = if (ProviderField.SERVICES in touched) Validators.chipsError(services, "un servicio") else null
    val productsError get() = if (ProviderField.PRODUCTS in touched) Validators.chipsError(products, "un tipo de producto") else null
    val schedulesError get() = if (ProviderField.SCHEDULES in touched) Validators.chipsError(schedules, "un horario") else null
    val cropsError get() = if (ProviderField.CROPS in touched) Validators.cropsListError(crops) else null
    val coverageError get() = if (ProviderField.COVERAGE in touched) Validators.coverageError(coverage) else null
    val capacityError get() = if (ProviderField.CAPACITY in touched) Validators.capacityError(capacity) else null
    val experienceError get() = if (ProviderField.EXPERIENCE in touched) Validators.experienceError(experience) else null

    val isValid: Boolean
        get() = Validators.chipsError(services, "") == null &&
                Validators.chipsError(products, "") == null &&
                Validators.chipsError(schedules, "") == null &&
                Validators.cropsListError(crops) == null &&
                Validators.coverageError(coverage) == null &&
                Validators.capacityError(capacity) == null &&
                Validators.experienceError(experience) == null
}

class ProviderProfileViewModel(app: Application) : AndroidViewModel(app) {

    private val db = AppDatabase.get(app)
    private val repo = ProviderProfileRepository(db.providerProfileDao())

    private val _state = MutableStateFlow(ProviderProfileUiState())
    val state: StateFlow<ProviderProfileUiState> = _state.asStateFlow()

    // el perfil guardado del aplicador con sesion abierta (null = todavia no lo completa);
    // la pantalla de inicio lo usa para avisar si falta completarlo
    @OptIn(ExperimentalCoroutinesApi::class)
    val savedProfile: StateFlow<ProviderProfile?> = db.userDao().activeUser()
        .flatMapLatest { user -> if (user == null) flowOf(null) else repo.profileOf(user.rut) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    // al abrir el formulario: carga el perfil guardado (o lo deja vacio si es la primera vez)
    fun load(ownerRut: String) {
        viewModelScope.launch {
            val p = repo.profileOf(ownerRut).first()
            _state.value = if (p == null) ProviderProfileUiState() else ProviderProfileUiState(
                services = p.services, products = p.products, schedules = p.schedules,
                crops = p.crops, coverage = p.coverageZones,
                capacity = p.capacityHaPerDay.toString(), experience = p.experienceYears.toString(),
                available = p.available
            )
        }
    }

    // marca o desmarca una opcion de los chips
    private fun Set<String>.toggle(v: String) = if (v in this) this - v else this + v
    fun onServiceToggle(v: String) = _state.update { it.copy(services = it.services.toggle(v), touched = it.touched + ProviderField.SERVICES) }
    fun onProductToggle(v: String) = _state.update { it.copy(products = it.products.toggle(v), touched = it.touched + ProviderField.PRODUCTS) }
    fun onScheduleToggle(v: String) = _state.update { it.copy(schedules = it.schedules.toggle(v), touched = it.touched + ProviderField.SCHEDULES) }

    fun onCropsChange(v: String) = _state.update { it.copy(crops = v) }
    fun onCoverageChange(v: String) = _state.update { it.copy(coverage = v) }
    // capacidad y experiencia solo aceptan digitos
    fun onCapacityChange(v: String) = _state.update { it.copy(capacity = v.filter(Char::isDigit).take(4)) }
    fun onExperienceChange(v: String) = _state.update { it.copy(experience = v.filter(Char::isDigit).take(2)) }
    fun onAvailableChange(v: Boolean) = _state.update { it.copy(available = v) }

    fun onFieldBlur(field: ProviderField) = _state.update { it.copy(touched = it.touched + field) }

    // valida todo; si esta bien guarda el perfil y avisa a la pantalla
    fun onSubmit(ownerRut: String, onSaved: () -> Unit) {
        _state.update { it.copy(touched = ProviderField.entries.toSet()) }
        val s = _state.value
        if (!s.isValid) return
        viewModelScope.launch {
            repo.save(
                ProviderProfile(
                    ownerRut = ownerRut, services = s.services, products = s.products,
                    schedules = s.schedules, crops = s.crops, coverageZones = s.coverage,
                    capacityHaPerDay = s.capacity.toInt(), experienceYears = s.experience.toInt(),
                    available = s.available
                )
            )
            onSaved()
        }
    }
}
