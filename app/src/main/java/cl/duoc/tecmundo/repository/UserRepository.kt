package cl.duoc.tecmundo.repository

import cl.duoc.tecmundo.model.LoginResult
import cl.duoc.tecmundo.model.PasswordHasher
import cl.duoc.tecmundo.model.ProfileType
import cl.duoc.tecmundo.model.UserAccount
import cl.duoc.tecmundo.model.UserDao
import cl.duoc.tecmundo.model.UserEntity
import cl.duoc.tecmundo.model.Validators
import cl.duoc.tecmundo.remote.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserRepository(private val dao: UserDao) {

    val isRegistered: Flow<Boolean> = dao.countUsers().map { it > 0 }

    val activeUser: Flow<UserAccount?> = dao.activeUser().map { it?.toAccount() }

    suspend fun emailExists(email: String): Boolean {
        val clean = email.trim()
        val remoteExists = try {
            RetrofitInstance.api.obtenerUsuarios().any { it.email.equals(clean, ignoreCase = true) }
        } catch (_: Exception) {
            false
        }
        return remoteExists || dao.emailExists(clean)
    }

    suspend fun rutExists(rut: String): Boolean {
        val remoteExists = try {
            RetrofitInstance.api.obtenerUsuarios().any { it.rut == rut }
        } catch (_: Exception) {
            false
        }
        return remoteExists || dao.rutExists(rut)
    }

    suspend fun saveAccount(account: UserAccount, password: String) {
        val salt = PasswordHasher.newSalt()
        val hash = withContext(Dispatchers.Default) { PasswordHasher.hash(password, salt) }
        val accountWithAuth = account.copy(passwordHash = hash, passwordSalt = salt)

        // 1. Guarda en Xano con la clave cifrada
        try {
            RetrofitInstance.api.crearUsuario(accountWithAuth)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 2. Guarda localmente en Room y abre la sesión
        val id = dao.insert(accountWithAuth.toEntity(hash, salt))
        dao.openSession(id)
    }

    suspend fun logIn(email: String, password: String, profile: ProfileType): LoginResult {
        val cleanEmail = email.trim()
        val localUser = dao.findByEmail(cleanEmail)
        // si el correo es de admin no se compara el perfil
        val isAdmin = Validators.isAdminEmail(cleanEmail)

        // Si ya existe en este teléfono, valida localmente
        if (localUser != null) {
            val passwordOk = withContext(Dispatchers.Default) {
                PasswordHasher.verify(password, localUser.passwordSalt, localUser.passwordHash)
            }
            if (!passwordOk) return LoginResult.InvalidCredentials
            if (!isAdmin && localUser.profile != profile.name) return LoginResult.NoProfile(profile)
            dao.openSession(localUser.id)
            return if (isAdmin) LoginResult.AdminSuccess else LoginResult.Success
        }

        // Si es otro móvil (o se borraron datos), consulta en Xano
        val remoteUser = try {
            RetrofitInstance.api.obtenerUsuarios()
                .firstOrNull { it.email.equals(cleanEmail, ignoreCase = true) }
        } catch (_: Exception) {
            null
        }

        val remoteHash = remoteUser?.passwordHash
        val remoteSalt = remoteUser?.passwordSalt
        if (remoteUser == null || remoteHash.isNullOrBlank() || remoteSalt.isNullOrBlank()) {
            return LoginResult.InvalidCredentials
        }

        val passwordOk = withContext(Dispatchers.Default) {
            PasswordHasher.verify(password, remoteSalt, remoteHash)
        }
        if (!passwordOk) return LoginResult.InvalidCredentials
        if (!isAdmin && remoteUser.profile != profile) return LoginResult.NoProfile(profile)

        // Guarda una copia en Room del nuevo móvil y abre sesión
        val newId = dao.insert(remoteUser.toEntity(remoteHash, remoteSalt))
        dao.openSession(newId)
        return if (isAdmin) LoginResult.AdminSuccess else LoginResult.Success
    }

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