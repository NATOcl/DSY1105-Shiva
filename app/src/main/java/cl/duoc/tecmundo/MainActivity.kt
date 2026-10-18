package cl.duoc.tecmundo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.duoc.tecmundo.ui.theme.TecMundoTheme
import cl.duoc.tecmundo.view.ShivaApp

// unica activity de la app, todo lo demas son pantallas compose
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // deja que la app dibuje debajo de las barras del sistema
        enableEdgeToEdge()
        setContent {
            TecMundoTheme {
                ShivaApp()
            }
        }
    }
}
