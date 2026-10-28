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
import androidx.compose.foundation.clickable
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.launch

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
fun HomeHeader(userName: String, greeting: String = "BIENVENIDA", onAvatarClick: () -> Unit = {}) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        // avatar: circulo cafe con la primera letra del nombre
        Box {
            Box(
                Modifier.size(40.dp).clip(CircleShape).background(ShivaColors.Brown).clickable(onClick = onAvatarClick),
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

// menu lateral del perfil: se abre al tocar el avatar y permite cerrar la sesion
// lo usan el productor y el aplicador, por eso vive aqui con las demas piezas compartidas
// el contenido recibe una funcion "abrir menu" para pasarsela al avatar del HomeHeader
@Composable
fun ProfileDrawerHost(
    userName: String,
    role: String,                 // "Productor" o "Aplicador", sale debajo del nombre
    onLogOut: () -> Unit,
    onProfile: (() -> Unit)? = null,   // si viene, el menu muestra "Mi perfil de prestador" (solo aplicador)
    content: @Composable (openDrawer: () -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val closeDrawer: () -> Unit = { scope.launch { drawerState.close() } }

    // el boton atras del telefono cierra el menu en vez de salir de la pantalla
    BackHandler(enabled = drawerState.isOpen) { closeDrawer() }

    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = Color.Black.copy(alpha = 0.4f),
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(290.dp),
                drawerContainerColor = ShivaColors.CreamSoft
            ) {
                Box(Modifier.fillMaxSize()) {
                    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 16.dp)) {
                        // logo chico de la app
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            SproutIcon(HomeColors.Title)
                            Spacer(Modifier.width(8.dp))
                            Text("Shiva", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = HomeColors.Title)
                        }
                        Spacer(Modifier.height(24.dp))
                        // avatar con anillo amarillo, nombre y tipo de perfil
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                Modifier.size(56.dp)
                                    .border(3.dp, ShivaColors.Yellow, CircleShape)
                                    .padding(4.dp).clip(CircleShape).background(ShivaColors.Brown),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(userName.firstOrNull()?.uppercase() ?: "", color = Color.White,
                                    fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(userName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = HomeColors.Title)
                                Text(role, fontSize = 12.sp, color = HomeColors.Subtitle)
                            }
                        }
                        // opcion de perfil de prestador, solo para el aplicador
                        if (onProfile != null) {
                            Spacer(Modifier.height(24.dp))
                            Row(
                                Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        closeDrawer()
                                        onProfile()
                                    }
                                    .padding(vertical = 12.dp, horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(Modifier.size(8.dp).background(ShivaColors.Yellow, CircleShape))
                                Spacer(Modifier.width(14.dp))
                                Text("Mi perfil de prestador", fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium, color = HomeColors.Title)
                            }
                        }
                        // empuja "Cerrar sesion" al final del menu
                        Spacer(Modifier.weight(1f))
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    closeDrawer()
                                    onLogOut()
                                }
                                .padding(vertical = 12.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LogoutIcon(ShivaColors.Error)
                            Spacer(Modifier.width(12.dp))
                            Text("Cerrar sesión", fontSize = 14.sp, fontWeight = FontWeight.Medium,
                                color = ShivaColors.Error)
                        }
                    }
                    // boton X amarillo arriba a la derecha del menu
                    Box(
                        Modifier.align(Alignment.TopEnd).padding(top = 8.dp, end = 8.dp)
                            .size(36.dp).clip(CircleShape).background(Color(0xFFFFF1C2))
                            .clickable(onClick = closeDrawer),
                        contentAlignment = Alignment.Center
                    ) {
                        CloseIcon(HomeColors.Title)
                    }
                }
            }
        }
    ) {
        content { scope.launch { drawerState.open() } }
    }
}

// brotecito dibujado a mano (tallo y dos hojas) para no depender de iconos extra
@Composable
private fun SproutIcon(color: Color) {
    Canvas(Modifier.size(16.dp)) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(width = 1.6.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(w / 2, h), Offset(w / 2, h * 0.4f), strokeWidth = stroke.width, cap = StrokeCap.Round)
        val left = Path().apply {
            moveTo(w / 2, h * 0.55f); quadraticBezierTo(w * 0.1f, h * 0.55f, w * 0.1f, h * 0.15f)
            quadraticBezierTo(w * 0.5f, h * 0.15f, w / 2, h * 0.55f)
        }
        val right = Path().apply {
            moveTo(w / 2, h * 0.4f); quadraticBezierTo(w * 0.9f, h * 0.4f, w * 0.9f, h * 0.05f)
            quadraticBezierTo(w * 0.5f, h * 0.05f, w / 2, h * 0.4f)
        }
        drawPath(left, color, style = stroke)
        drawPath(right, color, style = stroke)
    }
}

// la X de cerrar, dibujada a mano
@Composable
private fun CloseIcon(color: Color) {
    Canvas(Modifier.size(16.dp)) {
        val w = 2.dp.toPx()
        drawLine(color, Offset(0f, 0f), Offset(size.width, size.height), w, StrokeCap.Round)
        drawLine(color, Offset(size.width, 0f), Offset(0f, size.height), w, StrokeCap.Round)
    }
}

// icono de salir (puerta abierta con flecha), dibujado a mano
@Composable
private fun LogoutIcon(color: Color) {
    Canvas(Modifier.size(20.dp)) {
        val w = 1.8.dp.toPx()
        val s = size.width
        // marco de la puerta, abierto a la derecha
        val door = Path().apply {
            moveTo(s * 0.5f, s * 0.15f); lineTo(s * 0.15f, s * 0.15f)
            lineTo(s * 0.15f, s * 0.85f); lineTo(s * 0.5f, s * 0.85f)
        }
        drawPath(door, color, style = Stroke(width = w, cap = StrokeCap.Round))
        // flecha hacia afuera
        drawLine(color, Offset(s * 0.4f, s * 0.5f), Offset(s * 0.9f, s * 0.5f), w, StrokeCap.Round)
        drawLine(color, Offset(s * 0.9f, s * 0.5f), Offset(s * 0.72f, s * 0.32f), w, StrokeCap.Round)
        drawLine(color, Offset(s * 0.9f, s * 0.5f), Offset(s * 0.72f, s * 0.68f), w, StrokeCap.Round)
    }
}
