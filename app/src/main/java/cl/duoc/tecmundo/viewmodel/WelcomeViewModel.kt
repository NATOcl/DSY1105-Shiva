package cl.duoc.tecmundo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.tecmundo.model.AppDatabase
import cl.duoc.tecmundo.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

// controla la bienvenida: solo necesita saber si ya hay cuenta
class WelcomeViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = UserRepository(AppDatabase.get(app).userDao())

    // Room avisa solo cuando se registra una cuenta, no hace falta refrescar a mano
    val accountExists: StateFlow<Boolean> = repo.isRegistered
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)
}
