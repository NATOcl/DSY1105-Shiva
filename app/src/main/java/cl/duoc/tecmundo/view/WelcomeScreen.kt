package cl.duoc.tecmundo.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import cl.duoc.tecmundo.R
import cl.duoc.tecmundo.ui.theme.ShivaColors
import cl.duoc.tecmundo.viewmodel.WelcomeViewModel

// pantalla de inicio: titulo, botones y fondo de campo
@Composable
fun WelcomeScreen(
    onCreateAccount: () -> Unit,
    viewModel: WelcomeViewModel = viewModel()
) {
    // si ya hay cuenta, mostramos un aviso abajo
    val accountExists by viewModel.accountExists.collectAsStateWithLifecycle()

    // cada vez que se abre la bienvenida se revisa de nuevo si ya hay cuenta
    LaunchedEffect(Unit) { viewModel.refresh() }

    // capas: foto de fondo, degradado oscuro y encima el contenido
    Box(Modifier.fillMaxSize()) {
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

            // iniciar sesion: solo se ve, todavia no hace nada
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Color.White)
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

            // crear cuenta: el unico boton que funciona, lleva al registro
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
}
