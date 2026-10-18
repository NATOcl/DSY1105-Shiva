package cl.duoc.tecmundo.view

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

// las dos pantallas que existen por ahora
private enum class AppScreen { WELCOME, REGISTER }

// decide que pantalla mostrar (sin libreria de navegacion, con solo dos pantallas alcanza)
@Composable
fun ShivaApp() {
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.WELCOME) }

    // la bienvenida es oscura (iconos claros) y el registro es claro (iconos oscuros)
    val view = LocalView.current
    SideEffect {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
            currentScreen == AppScreen.REGISTER
    }

    when (currentScreen) {
        AppScreen.WELCOME -> WelcomeScreen(
            onCreateAccount = { currentScreen = AppScreen.REGISTER }
        )

        AppScreen.REGISTER -> {
            // el boton atras del telefono vuelve a la bienvenida
            BackHandler { currentScreen = AppScreen.WELCOME }
            RegisterScreen(
                onBack = { currentScreen = AppScreen.WELCOME },
                onRegistered = { currentScreen = AppScreen.WELCOME }
            )
        }
    }
}
