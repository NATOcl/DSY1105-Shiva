package cl.duoc.tecmundo.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.tecmundo.R
import cl.duoc.tecmundo.ui.theme.TecMundoTheme
import cl.duoc.tecmundo.ui.theme.TerranaGreenDark
import cl.duoc.tecmundo.ui.theme.TerranaLime
import cl.duoc.tecmundo.view.components.ArrowCircle
import cl.duoc.tecmundo.view.components.TerranaLogoBadge

/** VISTA: pantalla de bienvenida. El único elemento funcional es el botón "Comenzar ahora". */
@Composable
fun WelcomeScreen(onStartClick: () -> Unit, modifier: Modifier = Modifier) {
    val overlay = TerranaGreenDark
    Box(modifier = modifier.fillMaxSize().background(TerranaGreenDark)) {
        // Fondo: res/drawable-nodpi/bg_campo.jpg (ahora hay un placeholder .xml con el mismo nombre)
        Image(
            painter = painterResource(R.drawable.bg_campo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to overlay.copy(alpha = 0.35f),
                            0.40f to overlay.copy(alpha = 0f),
                            0.70f to overlay.copy(alpha = 0.70f),
                            1f to overlay.copy(alpha = 0.97f)
                        )
                    )
                )
        )

        // Logo superior
        Row(
            modifier = Modifier.statusBarsPadding().padding(start = 24.dp, top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TerranaLogoBadge()
            Spacer(Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.terrana_name),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Contenido inferior
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 24.dp, end = 24.dp, bottom = 28.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.width(28.dp).height(1.5.dp).background(TerranaLime))
                Spacer(Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.welcome_tagline),
                    color = TerranaLime,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            }
            Spacer(Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.welcome_title),
                color = Color.White,
                fontSize = 38.sp,
                lineHeight = 40.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.welcome_subtitle),
                color = Color(0xFFD9E6DD),
                fontSize = 15.sp,
                lineHeight = 21.sp
            )
            Spacer(Modifier.height(28.dp))

            // Botón funcional: navega al registro
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .clickable(onClick = onStartClick)
                    .padding(start = 20.dp, end = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.welcome_start),
                        color = TerranaGreenDark,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    ArrowCircle()
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.welcome_footer),
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFA9C2B3),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun WelcomeScreenPreview() {
    TecMundoTheme { WelcomeScreen(onStartClick = {}) }
}
