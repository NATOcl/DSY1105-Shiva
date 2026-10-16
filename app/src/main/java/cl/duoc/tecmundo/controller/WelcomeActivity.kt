package cl.duoc.tecmundo.controller

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.duoc.tecmundo.ui.theme.TecMundoTheme
import cl.duoc.tecmundo.view.WelcomeScreen

// Esta pantalla es la primera que se abre en la app
class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dejamos la barra de arriba y abajo transparentes sobre la foto
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        setContent {
            TecMundoTheme {
                WelcomeScreen(
                    // Al tocar "Comenzar ahora" nos vamos al registro
                    onStartClick = {
                        startActivity(Intent(this@WelcomeActivity, RegisterActivity::class.java))
                    }
                )
            }
        }
    }
}