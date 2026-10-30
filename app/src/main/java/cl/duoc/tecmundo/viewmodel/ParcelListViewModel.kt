package cl.duoc.tecmundo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.tecmundo.model.AppDatabase
import cl.duoc.tecmundo.model.Parcel
import cl.duoc.tecmundo.repository.ParcelRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// las parcelas del productor que tiene la sesion abierta
class ParcelListViewModel(app: Application) : AndroidViewModel(app) {

    private val db = AppDatabase.get(app)
    private val repo = ParcelRepository(db.parcelDao())

    // si cambia la sesion, la lista cambia sola (sin sesion = lista vacia)
    @OptIn(ExperimentalCoroutinesApi::class)
    val parcels: StateFlow<List<Parcel>> = db.userDao().activeUser()
        .flatMapLatest { user ->
            if (user == null) flowOf(emptyList()) else repo.parcelsOf(user.rut)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // elimina la parcela; launch la ejecuta en segundo plano
    // la lista "parcels" se actualiza sola porque viene de un Flow de Room
    fun deleteParcel(id: Long) {
        viewModelScope.launch { repo.delete(id) }
    }
}
