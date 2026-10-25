package cl.duoc.tecmundo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.tecmundo.model.AppDatabase
import cl.duoc.tecmundo.model.UserAccount
import cl.duoc.tecmundo.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// la sesion del usuario: quien esta dentro y como salir
class SessionViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = UserRepository(AppDatabase.get(app).userDao())

    // la cuenta con sesion abierta (null = nadie); Room la recuerda aunque se cierre la app
    val activeUser: StateFlow<UserAccount?> = repo.activeUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    // cierra la sesion; la cuenta y sus datos se mantienen guardados
    fun logOut() {
        viewModelScope.launch { repo.logOut() }
    }
}