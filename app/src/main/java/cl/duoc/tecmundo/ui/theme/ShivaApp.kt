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
import cl.duoc.tecmundo.ui.theme.ApplicatorHomeScreen
import cl.duoc.tecmundo.ui.theme.ProducerHomeScreen
import cl.duoc.tecmundo.viewmodel.SessionViewModel


// las tres pantallas que existen por ahora
private enum class AppScreen { WELCOME, REGISTER, LOGIN }

// decide que pantalla mostrar (sin libreria de navegacion, con tres pantallas alcanza)
@Composable
fun ShivaApp(session: SessionViewModel = viewModel()) {
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.WELCOME) }
    // el perfil que se eligio en la bienvenida para iniciar sesion
    var loginProfile by rememberSaveable { mutableStateOf(ProfileType.PRODUCTOR) }
    // la cuenta con sesion abierta (null = nadie), room la recuerda aunque se cierre la app
    val activeUser by session.activeUser.collectAsStateWithLifecycle()
    val user = activeUser


    // la bienvenida es oscura (iconos claros); registro y login son claros (iconos oscuros)
    val view = LocalView.current
    SideEffect {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
            user != null || currentScreen != AppScreen.WELCOME
    }
    if (user != null) {
        // hay sesion abierta: cada perfil ve su pantalla de inicio con su nombre
        when (user.profile) {
            ProfileType.PRODUCTOR -> ProducerHomeScreen(
                userName = user.fullName,
                onLogOut = { session.logOut() }
            )
            ProfileType.APLICADOR -> ApplicatorHomeScreen(
                userName = user.fullName,
                onLogOut = { session.logOut() }
            )
        }
    } else {

    when (currentScreen) {
        AppScreen.WELCOME -> WelcomeScreen(
            onCreateAccount = { currentScreen = AppScreen.REGISTER },
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
