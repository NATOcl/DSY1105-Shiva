package cl.duoc.tecmundo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.tecmundo.model.ApplicationRequest
import cl.duoc.tecmundo.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RequestListViewModel : ViewModel() {

    private val _requests = MutableStateFlow<List<ApplicationRequest>>(emptyList())
    val requests: StateFlow<List<ApplicationRequest>> = _requests.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                _requests.value = RetrofitInstance.api.obtenerSolicitudes()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun crearSolicitud(request: ApplicationRequest, onCreated: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.crearSolicitud(request)
                refresh()
                onCreated()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}