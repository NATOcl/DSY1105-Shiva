package cl.duoc.tecmundo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import cl.duoc.tecmundo.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// controla la bienvenida: solo necesita saber si ya hay cuenta
class WelcomeViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = UserRepository(app)
    private val _accountExists = MutableStateFlow(repo.isRegistered())
    val accountExists: StateFlow<Boolean> = _accountExists.asStateFlow()

    // se llama al volver a la pantalla, por si la cuenta se registro mientras tanto
    fun refresh() {
        _accountExists.value = repo.isRegistered()
    }
}
