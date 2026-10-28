package cl.duoc.tecmundo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.tecmundo.model.AppDatabase
import cl.duoc.tecmundo.model.Parcel
import cl.duoc.tecmundo.repository.ParcelRepository
import cl.duoc.tecmundo.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

// la lista de parcelas del productor que tiene la sesion abierta
class ParcelListViewModel(app: Application) : AndroidViewModel(app) {

    private val db = AppDatabase.get(app)
    private val userRepo = UserRepository(db.userDao())
    private val parcelRepo = ParcelRepository(db.parcelDao())

    // cada vez que cambia la cuenta activa, cambia la lista (si nadie tiene sesion, queda vacia)
    // y cada vez que se guarda una parcela nueva, la lista se actualiza sola
    @OptIn(ExperimentalCoroutinesApi::class)
    val parcels: StateFlow<List<Parcel>> = userRepo.activeUser
        .flatMapLatest { user ->
            if (user == null) flowOf(emptyList()) else parcelRepo.parcelsOf(user.rut)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}