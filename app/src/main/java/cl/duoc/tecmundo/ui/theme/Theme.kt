package cl.duoc.tecmundo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// esquema claro con la paleta de Shiva (sin colores dinamicos, para que siempre se vea igual)
private val ShivaColorScheme = lightColorScheme(
    primary = ShivaColors.Brown,
    secondary = ShivaColors.Yellow,
    error = ShivaColors.Error
)

// tema general que envuelve toda la app
@Composable
fun TecMundoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ShivaColorScheme,
        typography = Typography,
        content = content
    )
}
