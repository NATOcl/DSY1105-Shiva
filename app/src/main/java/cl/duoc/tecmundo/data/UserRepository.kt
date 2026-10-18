package cl.duoc.tecmundo.data

import android.content.Context
import androidx.core.content.edit
import cl.duoc.tecmundo.model.UserAccount

// guarda y lee los datos de la cuenta dentro del telefono
class UserRepository(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // avisa si ya hay una cuenta registrada (por defecto no)
    fun isRegistered(): Boolean = prefs.getBoolean(KEY_REGISTERED, false)

    // guarda los datos y marca que la cuenta ya existe
    fun saveAccount(account: UserAccount) {
        prefs.edit {
            putBoolean(KEY_REGISTERED, true)
            putString(KEY_PROFILE, account.profile.name)
            putString(KEY_NAME, account.fullName)
            putString(KEY_RUT, account.rut)
            putString(KEY_EMAIL, account.email)
            putString(KEY_PHONE, account.phone)
            putString(KEY_FARM, account.farmName)
        }
    }

    // nombre del archivo y de cada dato guardado
    private companion object {
        const val PREFS_NAME = "shiva_prefs"
        const val KEY_REGISTERED = "account_registered"
        const val KEY_PROFILE = "profile"
        const val KEY_NAME = "full_name"
        const val KEY_RUT = "rut"
        const val KEY_EMAIL = "email"
        const val KEY_PHONE = "phone"
        const val KEY_FARM = "farm_name"
    }
}
