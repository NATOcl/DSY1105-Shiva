package cl.duoc.tecmundo.controller

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.duoc.tecmundo.model.RegisterFormState
import cl.duoc.tecmundo.ui.theme.TecMundoTheme
import cl.duoc.tecmundo.view.RegisterScreen

// Esta pantalla maneja el formulario de registro
class RegisterActivity : ComponentActivity() {

    // Creamos el modelo que guarda los datos del formulario
    private val form = RegisterFormState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Barras transparentes con íconos oscuros porque el fondo es claro
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        setContent {
            TecMundoTheme {
                RegisterScreen(
                    form = form,
                    // La flecha de volver cierra esta pantalla
                    onBackClick = { finish() }
                )
            }
        }
    }
}