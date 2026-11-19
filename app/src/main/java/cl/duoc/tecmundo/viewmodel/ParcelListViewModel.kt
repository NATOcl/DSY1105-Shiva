package cl.duoc.tecmundo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.tecmundo.model.AppDatabase
import cl.duoc.tecmundo.model.Parcel
import cl.duoc.tecmundo.repository.ParcelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ParcelListViewModel(app: Application) : AndroidViewModel(app) {

    private val db = AppDatabase.get(app)
    private val repo = ParcelRepository()

    private val _parcels = MutableStateFlow<List<Parcel>>(emptyList())
    val parcels: StateFlow<List<Parcel>> = _parcels.asStateFlow()

    private var currentRut: String? = null

    init {
        viewModelScope.launch {
            db.userDao().activeUser().collect { user ->
                currentRut = user?.rut
                refresh()
            }
        }
    }

    fun refresh() {
        val rut = currentRut
        if (rut == null) {
            _parcels.value = emptyList()
            return
        }
        viewModelScope.launch {
            try {
                _parcels.value = repo.parcelsOf(rut)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteParcel(id: Long) {
        viewModelScope.launch {
            try {
                repo.delete(id)
                refresh()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}