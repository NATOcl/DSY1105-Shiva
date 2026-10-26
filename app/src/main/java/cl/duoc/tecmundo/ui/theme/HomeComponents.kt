package cl.duoc.tecmundo.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.tecmundo.ui.theme.ShivaColors

// piezas que comparten las pantallas de inicio del productor y del aplicador
// (asi no repetimos el mismo codigo en las dos)

// colores propios de estas pantallas, los demas estan en ShivaColors
object HomeColors {
    val Title = Color(0xFF0F3D2E)      // verde oscuro de los titulos
    val Eyebrow = Color(0xFF3B7A5F)    // verde de las etiquetas chicas
    val Subtitle = Color(0xFF6B7280)   // gris del texto de apoyo
    val Pill = Color(0xFFFDE97A)       // amarillo claro de los contadores
    val Pending = Color(0xFFC2410C)    // naranjo de "PENDIENTE"
    val Online = Color(0xFF22C55E)     // puntito verde del avatar
}

// fondo crema con degradado suave, igual que en los mockups
// recibe el contenido como lambda y lo pone dentro de una columna con los margenes
@Composable
fun HomeBackground(content: @Composable ColumnScope.() -> Unit) {
    Box(
        Modifier.fillMaxSize().background(
            Brush.linearGradient(listOf(Color(0xFFFFF1C2), ShivaColors.CreamSoft, ShivaColors.CreamSoft))
        )
    ) {
        Column(
            // statusBarsPadding y navigationBarsPadding evitan que quede tapado por las barras del sistema
            Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            content = content
        )
    }
}

// cabecera: avatar con inicial a la izquierda, saludo y nombre a la derecha
@Composable
fun HomeHeader(userName: String, greeting: String = "BIENVENIDA") {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        // avatar: circulo cafe con la primera letra del nombre
        Box {
            Box(
                Modifier.size(40.dp).clip(CircleShape).background(ShivaColors.Brown),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    userName.firstOrNull()?.uppercase() ?: "", // si el nombre viene vacio no se cae
                    color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp
                )
            }
            // puntito verde de conectado, con borde blanco para que se separe del avatar
            Box(
                Modifier.size(12.dp).align(Alignment.BottomEnd)
                    .background(Color.White, CircleShape).padding(2.dp)
                    .background(HomeColors.Online, CircleShape)
            )
        }
        // empuja el saludo hacia la derecha
        Spacer(Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.End) {
            Text(greeting, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp, color = HomeColors.Subtitle)
            Text(userName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = HomeColors.Title)
        }
    }
}

// pildora amarilla con el contador ("0 activas", "3 disponibles")
@Composable
fun CountPill(text: String) {
    Box(
        Modifier.clip(RoundedCornerShape(50)).background(HomeColors.Pill)
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(text, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ShivaColors.BrownDark)
    }
}
