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

// las tres pantallas que existen por el momento
private enum class AppScreen { WELCOME, REGISTER, LOGIN }

// decide que pantalla mostrar (sin libreria de navegacion, con tres pantallas alcanza)
@Composable
fun ShivaApp() {
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.WELCOME) }
    // el perfil que se eligio en la bienvenida para iniciar sesion
    var loginProfile by rememberSaveable { mutableStateOf(ProfileType.PRODUCTOR) }

    // la bienvenida es oscura (iconos claros); registro y login son claros (iconos oscuros)
    val view = LocalView.current
    SideEffect {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
            currentScreen != AppScreen.WELCOME
    }

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
