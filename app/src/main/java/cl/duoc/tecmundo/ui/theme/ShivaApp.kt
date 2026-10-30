package cl.duoc.tecmundo.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import cl.duoc.tecmundo.model.ProfileType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.tecmundo.ui.theme.AdminHomeScreen
import cl.duoc.tecmundo.ui.theme.ApplicatorHomeScreen
import cl.duoc.tecmundo.ui.theme.ProducerHomeScreen
import cl.duoc.tecmundo.viewmodel.SessionViewModel
import cl.duoc.tecmundo.ui.theme.NewParcelScreen
import cl.duoc.tecmundo.viewmodel.ParcelListViewModel
import cl.duoc.tecmundo.viewmodel.ProviderProfileViewModel
import cl.duoc.tecmundo.ui.theme.ProviderProfileScreen


// las tres pantallas que existen por ahora
private enum class AppScreen { WELCOME, REGISTER, LOGIN }

// decide que pantalla mostrar (sin libreria de navegacion, con tres pantallas alcanza)
@Composable
fun ShivaApp(
    session: SessionViewModel = viewModel(),
    parcelList: ParcelListViewModel = viewModel(),
    providerProfile: ProviderProfileViewModel = viewModel()
) {
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.WELCOME) }
    // el perfil que se eligio en la bienvenida para iniciar sesion

    var loginProfile by rememberSaveable { mutableStateOf(ProfileType.PRODUCTOR) }

    // true cuando se entro como administrador (no tiene cuenta ni login, no se guarda en Room)
    var adminMode by rememberSaveable { mutableStateOf(false) }

    // la cuenta con sesion abierta (null = nadie), room la recuerda aunque se cierre la app
    val activeUser by session.activeUser.collectAsStateWithLifecycle()
    val user = activeUser
    // true cuando el productor tiene abierto el formulario de nueva parcela
    var addingParcel by rememberSaveable { mutableStateOf(false) }
    // true cuando el aplicador tiene abierto el formulario de su perfil de prestador
    var editingProfile by rememberSaveable { mutableStateOf(false) }
    // el perfil de prestador guardado (null = todavia no lo completa)
    val savedProfile by providerProfile.savedProfile.collectAsStateWithLifecycle()
    // las parcelas del productor con sesion abierta (se actualiza sola al guardar)
    val parcels by parcelList.parcels.collectAsStateWithLifecycle()


    // la bienvenida es oscura (iconos claros); registro y login son claros (iconos oscuros)
    val view = LocalView.current
    SideEffect {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
            user != null || adminMode || currentScreen != AppScreen.WELCOME
    }
    if (adminMode) {
        // el administrador entra directo desde la bienvenida; salir lo devuelve a ella
        AdminHomeScreen(onLogOut = { adminMode = false })
    } else if (user != null) {
        // hay sesion abierta: cada perfil ve su pantalla de inicio con su nombre
        when (user.profile) {
            ProfileType.PRODUCTOR -> {
                if (addingParcel) {
                    // el boton atras del telefono cierra el formulario sin guardar
                    BackHandler { addingParcel = false }
                    NewParcelScreen(
                        ownerRut = user.rut,
                        onBack = { addingParcel = false },
                        onSaved = { addingParcel = false }
                    )
                } else {
                    ProducerHomeScreen(
                        userName = user.fullName,
                        parcels = parcels,
                        onAddParcel = { addingParcel = true },
                        onLogOut = { session.logOut() }
                    )
                }
            }
            ProfileType.APLICADOR -> {
                if (editingProfile) {
                    // el boton atras del telefono cierra el formulario sin guardar
                    BackHandler { editingProfile = false }
                    ProviderProfileScreen(
                        ownerRut = user.rut,
                        onBack = { editingProfile = false },
                        onSaved = { editingProfile = false }
                    )
                } else {
                    ApplicatorHomeScreen(
                        userName = user.fullName,
                        hasProfile = savedProfile != null,
                        onEditProfile = { editingProfile = true },
                        onLogOut = { session.logOut() }
                    )
                }
            }
        }
    } else {

        when (currentScreen) {
            AppScreen.WELCOME -> WelcomeScreen(
                onCreateAccount = { currentScreen = AppScreen.REGISTER },
                onAdmin = { adminMode = true },
                onLogin = { profile ->
                    loginProfile = profile
                    currentScreen = AppScreen.LOGIN
                }
            )

            AppScreen.REGISTER -> {
                // el boton atras del telefono vuelve a la bienvenida
                BackHandler { currentScreen = AppScreen.WELCOME }
                RegisterScreen(
                    onBack = { currentScreen = AppScreen.WELCOME },
                    onRegistered = { currentScreen = AppScreen.WELCOME }
                )
            }

            AppScreen.LOGIN -> {
                BackHandler { currentScreen = AppScreen.WELCOME }
                LoginScreen(
                    profile = loginProfile,
                    onBack = { currentScreen = AppScreen.WELCOME },
                    onLoggedIn = { currentScreen = AppScreen.WELCOME }
                )
            }
        }
    }
}
