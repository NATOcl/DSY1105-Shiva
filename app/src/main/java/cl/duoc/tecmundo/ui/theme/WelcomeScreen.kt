package cl.duoc.tecmundo.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import cl.duoc.tecmundo.R
import cl.duoc.tecmundo.model.ProfileType
import cl.duoc.tecmundo.ui.theme.ShivaColors
import cl.duoc.tecmundo.viewmodel.WelcomeViewModel

// pantalla de inicio: titulo, botones y fondo de campo
@Composable
fun WelcomeScreen(
    onCreateAccount: () -> Unit,
    onLogin: (ProfileType) -> Unit,
    viewModel: WelcomeViewModel = viewModel()
) {
    // si ya hay cuenta, mostramos un aviso abajo
    val accountExists by viewModel.accountExists.collectAsStateWithLifecycle()

    // si el selector de perfil esta abierto
    var showLogin by rememberSaveable { mutableStateOf(false) }

    // mientras se elige el perfil, la bienvenida de fondo se desenfoca
    val blurRadius by animateDpAsState(if (showLogin) 12.dp else 0.dp, label = "blur")

    // capas: foto de fondo, degradado oscuro y encima el contenido
    Box(Modifier.fillMaxSize().blur(blurRadius)) {
        // foto de trigo de fondo (esta en drawable-nodpi y pesa poco para no gastar memoria)
        Image(
            painter = painterResource(R.drawable.bg_wheat),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            // se alinea abajo para mostrar el camino y recortar el cielo
            alignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()
        )
        // degradado oscuro para que el texto se lea bien
        Box(
            Modifier.fillMaxSize().background(
                Brush.verticalGradient(
                    0f to Color(0xB3000000),
                    0.5f to Color(0x66000000),
                    1f to ShivaColors.ForestDark
                )
            )
        )

        Column(
            Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // logo y nombre de la app
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(ShivaColors.Yellow),
                    contentAlignment = Alignment.Center
                ) { Icon(painterResource(R.drawable.ic_logo), null, tint = ShivaColors.BrownDark, modifier = Modifier.size(22.dp)) }
                Spacer(Modifier.width(10.dp))
                Text("Shiva", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            Spacer(Modifier.height(36.dp))
            // titulo y bajada principal
            Text("— AGRICULTURA INTELIGENTE", color = ShivaColors.Lime, fontWeight = FontWeight.Bold,
                fontSize = 12.sp, letterSpacing = 2.sp)
            Spacer(Modifier.height(12.dp))
            Text("Tu campo,\nbajo control.", color = Color.White, fontWeight = FontWeight.ExtraBold,
                fontSize = 42.sp, lineHeight = 44.sp, letterSpacing = (-1).sp)
            Spacer(Modifier.height(12.dp))
            Text("Organiza tus parcelas, registra actividades y toma mejores decisiones en cada cultivo.",
                color = Color(0xCCFFFFFF), fontSize = 15.sp, lineHeight = 21.sp)

            // empuja los botones hacia abajo
            Spacer(Modifier.weight(1f))

            // aviso cuando ya hay una cuenta guardada
            if (accountExists) {
                Text(
                    "Ya hay una cuenta registrada en este dispositivo.",
                    color = ShivaColors.Lime, fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    textAlign = TextAlign.Center
                )
            }

            // iniciar sesion: abre el selector para elegir con que perfil entrar
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Color.White)
                    .clickable { showLogin = true }
                    .padding(start = 20.dp, end = 12.dp).height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Iniciar sesión", fontWeight = FontWeight.SemiBold, fontSize = 15.sp,
                    color = Color(0xFF1F2937), modifier = Modifier.weight(1f))
                Box(Modifier.size(36.dp).clip(CircleShape).background(ShivaColors.Yellow),
                    contentAlignment = Alignment.Center) {
                    Icon(painterResource(R.drawable.ic_arrow_right), null, tint = ShivaColors.BrownDark)
                }
            }

            Spacer(Modifier.height(12.dp))

            // crear cuenta: lleva al registro
            Box(
                Modifier.fillMaxWidth().height(52.dp).clip(RoundedCornerShape(20.dp))
                    .background(ShivaColors.Yellow).clickable(onClick = onCreateAccount),
                contentAlignment = Alignment.Center
            ) {
                Text("Crear cuenta", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = ShivaColors.BrownDark)
            }

            Spacer(Modifier.height(12.dp))
            // frase final
            Text("Cultiva información. Cosecha resultados.", color = Color(0xB3FFFFFF), fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
    }

    // selector de perfil (productor o aplicador)
    if (showLogin) {
        LoginProfileSheet(
            onSelect = { profile ->
                // elegido el perfil, se pasa a pedir correo y clave
                showLogin = false
                onLogin(profile)
            },
            onDismiss = { showLogin = false }
        )
    }
}

// hoja que sube desde abajo: "¿Como quieres entrar?" con los dos perfiles
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginProfileSheet(
    onSelect: (ProfileType) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    // primero se baja la hoja con animacion y despues se hace lo que corresponda
    fun closeThen(action: () -> Unit) {
        scope.launch {
            sheetState.hide()
            action()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ShivaColors.CreamSoft,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = null
    ) {
        Column(
            Modifier.fillMaxWidth().navigationBarsPadding()
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 16.dp)
        ) {
            // titulo y boton para cerrar
            Row(verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text("BIENVENIDO DE VUELTA", color = Color(0xFF2F6B4F), fontWeight = FontWeight.Bold,
                        fontSize = 12.sp, letterSpacing = 1.5.sp)
                    Spacer(Modifier.height(6.dp))
                    Text("¿Cómo quieres entrar?", color = ShivaColors.ForestDark,
                        fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                }
                Box(
                    Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFFFF1BF))
                        .clickable { closeThen(onDismiss) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painterResource(R.drawable.ic_close), "Cerrar",
                        tint = ShivaColors.ForestDark, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("Selecciona tu perfil para acceder directamente a tu espacio.",
                color = Color(0xFF6B7280), fontSize = 14.sp, lineHeight = 20.sp)
            Spacer(Modifier.height(20.dp))

            ProfileOption(R.drawable.ic_logo, "Entrar como productor",
                "Gestiona parcelas y publica solicitudes") { closeThen { onSelect(ProfileType.PRODUCTOR) } }
            Spacer(Modifier.height(12.dp))
            ProfileOption(R.drawable.ic_drone, "Entrar como aplicador",
                "Revisa trabajos y envía ofertas") { closeThen { onSelect(ProfileType.APLICADOR) } }
        }
    }
}

// una tarjeta de perfil: icono amarillo, titulo, detalle y flecha
@Composable
private fun ProfileOption(icon: Int, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Color.White)
            .border(BorderStroke(1.dp, ShivaColors.Border), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick).padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(ShivaColors.Yellow),
            contentAlignment = Alignment.Center
        ) { Icon(painterResource(icon), null, tint = ShivaColors.BrownDark, modifier = Modifier.size(22.dp)) }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = ShivaColors.ForestDark, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(subtitle, color = Color(0xFF6B7280), fontSize = 12.sp)
        }
        Icon(painterResource(R.drawable.ic_arrow_right), null, tint = ShivaColors.ForestDark)
    }
}
