package cl.duoc.tecmundo.ui.theme

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.tecmundo.model.Parcel

// pantalla de detalle de una parcela: se abre al tocar una tarjeta de "Mis parcelas"
@Composable
fun ParcelDetailScreen(
    parcel: Parcel,          // la parcela que se toco
    hasRequest: Boolean,     // true si ya publico una solicitud (para mostrarla en "Actividad reciente")
    onBack: () -> Unit,      // que pasa al tocar la flecha de volver
    onDelete: () -> Unit     // NUEVO: que pasa cuando el usuario confirma que quiere eliminar
) {
    // convierte la ruta de la foto en imagen (null si la parcela no tiene foto)
    val imagen = remember(parcel.photoPath) {
        parcel.photoPath?.let {
            BitmapFactory.decodeFile(it, BitmapFactory.Options().apply { inSampleSize = 4 })
                ?.asImageBitmap()
        }
    }

    // true cuando se muestra el cuadro "¿Seguro que quieres eliminar?"
    var showDeleteDialog by remember { mutableStateOf(false) }

    // toda la pantalla es una columna con scroll, sobre fondo crema
    Column(
        Modifier.fillMaxSize().background(ShivaColors.CreamSoft).verticalScroll(rememberScrollState())
    ) {
        // ---------- foto de arriba (con las esquinas de abajo redondeadas) ----------
        Box(
            Modifier.fillMaxWidth().height(280.dp)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(ShivaColors.BrownDark)
        ) {
            if (imagen != null) {
                Image(imagen, "Foto de la parcela", Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop)
            }
            // degradado oscuro hacia abajo para que se lean los textos blancos
            Box(
                Modifier.matchParentSize().background(
                    Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)))
                )
            )
            // boton circular para volver (arriba a la izquierda, debajo de la barra de estado)
            Box(
                Modifier.align(Alignment.TopStart).statusBarsPadding().padding(16.dp)
                    .size(40.dp).clip(CircleShape).background(Color.Black.copy(alpha = 0.4f))
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) { Text("‹", fontSize = 26.sp, color = Color.White) }

            // textos abajo a la izquierda: cultivo, nombre y zona
            Column(Modifier.align(Alignment.BottomStart).padding(20.dp)) {
                Text(parcel.crop.uppercase(), fontSize = 12.sp, fontWeight = FontWeight.Bold,
                    color = ShivaColors.Yellow, letterSpacing = 1.5.sp)
                Spacer(Modifier.height(4.dp))
                Text(parcel.name, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Spacer(Modifier.height(4.dp))
                // la zona solo se muestra si la parcela la tiene
                Text(
                    if (parcel.zone.isNotBlank()) "${parcel.zone} · Actualizado hoy" else "Actualizado hoy",
                    fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // ---------- los dos recuadros: superficie y tipo de cultivo ----------
        Row(
            Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DetailStat("Superficie", "${parcel.hectares} ha", Modifier.weight(1f))
            DetailStat("Tipo de cultivo", parcel.crop, Modifier.weight(1f))
        }

        Spacer(Modifier.height(24.dp))

        // ---------- actividad reciente ----------
        Text("Actividad reciente", fontSize = 18.sp, fontWeight = FontWeight.Bold,
            color = HomeColors.Title, modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(Modifier.height(12.dp))
        Column(Modifier.padding(start = 20.dp, end = 20.dp, bottom = 24.dp)) {
            if (hasRequest) {
                ActivityItem("Solicitud de aplicación publicada", "Hoy · Recién publicada")
            } else {
                Text("Todavía no hay actividad en esta parcela.", fontSize = 13.sp,
                    color = HomeColors.Subtitle)
            }
        }

        // ---------- boton eliminar parcela ----------
        Box(
            Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, bottom = 24.dp)
                .navigationBarsPadding()                       // para que no lo tape la barra del telefono
                .height(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, ShivaColors.Error, RoundedCornerShape(16.dp))   // borde rojo
                .clickable { showDeleteDialog = true },        // solo abre la pregunta, todavia no borra
            contentAlignment = Alignment.Center
        ) {
            Text("Eliminar parcela", fontSize = 14.sp, fontWeight = FontWeight.Bold,
                color = ShivaColors.Error)
        }

        // cuadro de confirmacion: solo se dibuja cuando showDeleteDialog es true
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },   // tocar fuera = cancelar
                title = { Text("Eliminar parcela") },
                text = { Text("¿Seguro que quieres eliminar \"${parcel.name}\"? Esta acción no se puede deshacer.") },
                confirmButton = {
                    // SI: cierra el cuadro y avisa que se confirmo el borrado
                    TextButton(onClick = {
                        showDeleteDialog = false
                        onDelete()
                    }) { Text("Sí", color = ShivaColors.Error, fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    // NO: solo cierra el cuadro, no pasa nada mas
                    TextButton(onClick = { showDeleteDialog = false }) { Text("No") }
                }
            )
        }
    }
}

// recuadro blanco con un dato (etiqueta chica arriba, valor abajo)
@Composable
private fun DetailStat(label: String, value: String, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(16.dp)
    Column(modifier.shadow(2.dp, shape).background(Color.White, shape).padding(14.dp)) {
        Text(label, fontSize = 11.sp, color = HomeColors.Subtitle)
        Spacer(Modifier.height(4.dp))
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = HomeColors.Title)
    }
}

// una fila de la lista de actividad: puntito amarillo + titulo + detalle
@Composable
private fun ActivityItem(title: String, detail: String) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        Modifier.fillMaxWidth().shadow(2.dp, shape).background(Color.White, shape).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(10.dp).clip(CircleShape).background(ShivaColors.Yellow))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = HomeColors.Title)
            Spacer(Modifier.height(2.dp))
            Text(detail, fontSize = 12.sp, color = HomeColors.Subtitle)
        }
    }
}
