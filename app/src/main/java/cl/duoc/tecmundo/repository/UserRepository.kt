package cl.duoc.tecmundo.repository

import cl.duoc.tecmundo.model.LoginResult
import cl.duoc.tecmundo.model.PasswordHasher
import cl.duoc.tecmundo.model.ProfileType
import cl.duoc.tecmundo.model.UserAccount
import cl.duoc.tecmundo.model.UserDao
import cl.duoc.tecmundo.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

// guarda y lee las cuentas y la sesion desde la base de datos Room
class UserRepository(private val dao: UserDao) {

    // avisa si ya hay una cuenta registrada (y se actualiza solo al registrar)
    val isRegistered: Flow<Boolean> = dao.countUsers().map { it > 0 }

    // la cuenta con sesion abierta, o null; sigue ahi aunque se cierre la app
    val activeUser: Flow<UserAccount?> = dao.activeUser().map { it?.toAccount() }

    // guarda la cuenta nueva (con la clave cifrada) y deja la sesion abierta
    suspend fun saveAccount(account: UserAccount, password: String) {
        val salt = PasswordHasher.newSalt()
        // cifrar la clave tarda un poco, por eso va fuera del hilo principal
        val hash = withContext(Dispatchers.Default) { PasswordHasher.hash(password, salt) }
        val id = dao.insert(account.toEntity(hash, salt))
        dao.openSession(id)
    }

    // inicia sesion con correo, clave y el perfil elegido
    suspend fun logIn(email: String, password: String, profile: ProfileType): LoginResult {
        val user = dao.findByEmail(email.trim())
        // mismo aviso si falla el correo o la clave, para no dar pistas
        val passwordOk = user != null && withContext(Dispatchers.Default) {
            PasswordHasher.verify(password, user.passwordSalt, user.passwordHash)
        }
        if (user == null || !passwordOk) return LoginResult.InvalidCredentials
        // la cuenta existe, pero no es del perfil que eligio
        if (user.profile != profile.name) return LoginResult.NoProfile(profile)
        dao.openSession(user.id)
        return LoginResult.Success
    }

    // cierra la sesion (la cuenta queda guardada en el telefono)
    suspend fun logOut() = dao.closeSession()

    private fun UserAccount.toEntity(passwordHash: String, passwordSalt: String) = UserEntity(
        profile = profile.name,
        fullName = fullName,
        rut = rut,
        email = email,
        phone = phone,
        farmName = farmName,
        passwordHash = passwordHash,
        passwordSalt = passwordSalt
    )

    private fun UserEntity.toAccount() = UserAccount(
        profile = ProfileType.valueOf(profile),
        fullName = fullName,
        rut = rut,
        email = email,
        phone = phone,
        farmName = farmName
    )
}
