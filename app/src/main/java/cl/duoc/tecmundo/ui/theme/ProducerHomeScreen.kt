package cl.duoc.tecmundo.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.tecmundo.model.Parcel
import cl.duoc.tecmundo.ui.theme.ShivaColors
import cl.duoc.tecmundo.ui.theme.TecMundoTheme
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale

// pantalla de inicio del PRODUCTOR: lista de sus parcelas y boton para anadir una
@Composable
fun ProducerHomeScreen(
    userName: String,                       // nombre que sale arriba a la derecha
    parcels: List<Parcel> = emptyList(),    // parcelas del productor, vacia si todavia no registra ninguna
    onAddParcel: () -> Unit = {},           // que pasa al tocar "Anadir nueva parcela"
    onLogOut: () -> Unit = {}                 // se llama al tocar "Cerrar sesión" en el menu lateral
) {
    // el avatar abre el menu lateral del perfil, donde esta el boton para cerrar sesion
    ProfileDrawerHost(userName = userName, role = "Productor", onLogOut = onLogOut) { openDrawer ->
        HomeBackground {
            // cabecera con avatar y nombre
            HomeHeader(userName = userName, greeting = "BIENVENIDA", onAvatarClick = openDrawer)

            Spacer(Modifier.height(20.dp))
            // etiqueta chica de arriba del titulo
            Text("VISTA GENERAL", fontSize = 11.sp, fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp, color = HomeColors.Eyebrow)
            Spacer(Modifier.height(6.dp))

            // titulo y contador de parcelas activas (el contador sale del tamano de la lista)
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Mis parcelas", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold,
                    color = HomeColors.Title, letterSpacing = (-0.5).sp, modifier = Modifier.weight(1f))
                CountPill(if (parcels.size == 1) "1 activa" else "${parcels.size} activas")
            }
            Spacer(Modifier.height(8.dp))
            Text("Revisa tus cultivos o publica una solicitud de aplicación.",
                fontSize = 14.sp, color = HomeColors.Subtitle, lineHeight = 20.sp)

            Spacer(Modifier.height(20.dp))
            // lista con scroll: primero las parcelas y al final el boton de anadir
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(parcels, key = { it.id }) { ParcelCard(it) }
                item { AddParcelCard(onAddParcel) }
            }
        }
    }
}

// tarjeta con borde punteado para anadir una parcela nueva
@Composable
private fun AddParcelCard(onClick: () -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Row(
        Modifier.fillMaxWidth().clip(shape).background(Color(0xFFFFF7DB))
            .clickable(onClick = onClick)
            .drawBehind {
                // dibuja el borde punteado (compose no trae uno hecho, se dibuja a mano)
                drawRoundRect(
                    color = Color(0xFFE4C95A),
                    cornerRadius = CornerRadius(20.dp.toPx()),
                    style = Stroke(
                        width = 1.dp.toPx(),
                        // 10 de linea y 8 de espacio, se repite
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f))
                    )
                )
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // circulo amarillo con el signo mas
        Box(
            Modifier.size(44.dp).clip(CircleShape).background(ShivaColors.Yellow),
            contentAlignment = Alignment.Center
        ) { Text("+", fontSize = 26.sp, fontWeight = FontWeight.Medium, color = ShivaColors.BrownDark) }
        Spacer(Modifier.width(14.dp))
        // textos de la tarjeta
        Column {
            Text("Añadir nueva parcela", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = HomeColors.Title)
            Spacer(Modifier.height(2.dp))
            Text("Registra ubicación, cultivo y superficie", fontSize = 12.sp, color = ShivaColors.BrownText)
        }
    }
}
// tarjeta de una parcela: la foto es el fondo, con los datos encima
@Composable
private fun ParcelCard(parcel: Parcel) {
    // convierte la ruta de la foto en imagen (null si la parcela no tiene foto)
    val imagen = remember(parcel.photoPath) {
        parcel.photoPath?.let {
            BitmapFactory.decodeFile(it, BitmapFactory.Options().apply { inSampleSize = 4 })
                ?.asImageBitmap()
        }
    }

    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp))) {

        // parte de arriba: foto de fondo + capa oscura + textos
        Box(Modifier.fillMaxWidth().background(Color(0xFF4A3A00))) {
            if (imagen != null) {
                Image(imagen, "Foto de la parcela", Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop)
            }
            // capa oscura para que se lean los textos
            Box(Modifier.matchParentSize().background(Color.Black.copy(alpha = 0.45f)))

            Column(Modifier.padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        // la zona solo se muestra si la parcela la tiene
                        if (parcel.zone.isNotBlank()) {
                            Text(parcel.zone.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Bold,
                                color = ShivaColors.Yellow, letterSpacing = 1.sp)
                            Spacer(Modifier.height(2.dp))
                        }
                        Text(parcel.name, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold,
                            color = Color.White)
                    }
                    // boton circular con la flecha
                    Box(
                        Modifier.size(36.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) { Text("›", fontSize = 22.sp, color = Color.White) }
                }

                Spacer(Modifier.height(16.dp))
                // recuadros con los datos de la parcela
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        InfoChip("CULTIVO", parcel.crop, Modifier.weight(1f))
                        InfoChip("SUPERFICIE", "${parcel.hectares} ha", Modifier.weight(1f))
                    }
                    InfoChip("DISPONE DE AGUA", if (parcel.hasWater) "Sí" else "No", Modifier.fillMaxWidth())
                    // los obstáculos solo se muestran si el productor los escribió
                    if (parcel.obstacles.isNotBlank()) {
                        InfoChip("OBSTÁCULOS", parcel.obstacles, Modifier.fillMaxWidth())
                    }
                }
            }   // <-- ESTA LLAVE FALTABA (cierra la columna con los textos)
        }       // cierra la caja con la foto de fondo

        // barra amarilla de abajo (todavia no hace nada, se conecta mas adelante)
        Box(
            Modifier.fillMaxWidth().background(ShivaColors.Yellow).padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("+  Solicitar aplicación", fontSize = 13.sp, fontWeight = FontWeight.Bold,
                color = ShivaColors.BrownDark)
        }
    }
}

// recuadro semitransparente con un dato (etiqueta chica arriba, valor abajo)
@Composable
private fun InfoChip(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier.clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.18f))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold,
            color = Color.White.copy(alpha = 0.7f), letterSpacing = 0.5.sp)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

// preview con la lista vacia
@Preview(showBackground = true)
@Composable
private fun ProducerHomeEmptyPreview() {
    TecMundoTheme { ProducerHomeScreen(userName = "María González") }
}

// preview con una parcela de ejemplo para ver como queda la tarjeta
@Preview(showBackground = true)
@Composable
private fun ProducerHomeWithParcelsPreview() {
    TecMundoTheme {
        ProducerHomeScreen(
            userName = "María González",
            parcels = listOf(Parcel(1, "Fundo Santa Clara", "Maíz", 10))
        )
    }
}