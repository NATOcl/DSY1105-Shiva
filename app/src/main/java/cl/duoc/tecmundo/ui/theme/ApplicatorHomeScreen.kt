package cl.duoc.tecmundo.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.tecmundo.R
import cl.duoc.tecmundo.model.ApplicationRequest
import cl.duoc.tecmundo.ui.theme.ShivaColors
import cl.duoc.tecmundo.ui.theme.TecMundoTheme

// datos de ejemplo mientras no hay base de datos de solicitudes
private val sampleRequests = listOf(
    ApplicationRequest(1, "Fundo Santa Clara", 12, "PENDIENTE", "Granulado", 10, "Diurna"),
    ApplicationRequest(2, "Parcela Los Aromos", 20, "PENDIENTE", "Líquido", 14, "Nocturna"),
    ApplicationRequest(3, "Campo Vista Sur", 27, "PENDIENTE", "Líquido", 8, "Diurna")
)

// pantalla de inicio del APLICADOR: solicitudes cercanas de productores
@Composable
fun ApplicatorHomeScreen(
    userName: String,                                   // nombre que sale arriba a la derecha
    requests: List<ApplicationRequest> = sampleRequests, // solicitudes a mostrar
    onRequestClick: (ApplicationRequest) -> Unit = {},   // que pasa al tocar una tarjeta
    onLogOut: () -> Unit = {}                           // cierra la sesion al tocar el avatar
) {
    HomeBackground {
        // cabecera con avatar y nombre
        HomeHeader(userName = userName, greeting = "BIENVENIDO", onAvatarClick = onLogOut)

        Spacer(Modifier.height(20.dp))
        // titulo de la pantalla y bajada
        Text("OPORTUNIDADES CERCANAS", fontSize = 13.sp, fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp, color = HomeColors.Eyebrow)
        Spacer(Modifier.height(8.dp))
        Text("Revisa solicitudes cercanas y envía tu oferta al productor.",
            fontSize = 15.sp, color = HomeColors.Subtitle, lineHeight = 22.sp)
        Spacer(Modifier.height(12.dp))

        // contador alineado a la derecha
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            CountPill("${requests.size} disponibles")
        }

        Spacer(Modifier.height(16.dp))
        // lista con scroll, una tarjeta por solicitud
        LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            items(requests, key = { it.id }) { RequestCard(it, onClick = { onRequestClick(it) }) }
        }
    }
}

// tarjeta de una solicitud: foto a la izquierda, datos a la derecha
@Composable
private fun RequestCard(request: ApplicationRequest, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().height(130.dp).clip(RoundedCornerShape(20.dp))
            .background(Color.White).clickable(onClick = onClick)
    ) {
        // foto del campo (si no hay una propia se usa la de trigo)
        Image(
            painter = painterResource(request.imageRes ?: R.drawable.bg_wheat),
            contentDescription = request.farmName,
            contentScale = ContentScale.Crop, // recorta para llenar el espacio sin deformar
            modifier = Modifier.width(78.dp).fillMaxHeight()
        )
        Column(Modifier.weight(1f).padding(horizontal = 14.dp, vertical = 14.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                // distancia, estado y nombre del campo
                Column(Modifier.weight(1f)) {
                    Text("A ${request.distanceKm} KM · ${request.status}", fontSize = 10.sp,
                        fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = HomeColors.Pending)
                    Spacer(Modifier.height(6.dp))
                    Text(request.farmName, fontSize = 16.sp, fontWeight = FontWeight.Bold,
                        color = HomeColors.Title)
                }
                // boton circular con flecha
                Box(
                    Modifier.size(28.dp).clip(CircleShape).background(ShivaColors.Yellow),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painterResource(R.drawable.ic_arrow_right), "Ver solicitud",
                        tint = ShivaColors.BrownDark, modifier = Modifier.size(14.dp))
                }
            }
            // empuja los detalles hacia abajo de la tarjeta
            Spacer(Modifier.weight(1f))
            // producto, superficie y tipo de aplicacion
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Detail("Producto", request.product)
                Detail("Superficie", "${request.hectares} ha")
                Detail("Aplicación", request.schedule)
            }
        }
    }
}

// par etiqueta + valor chico (ej: Producto / Granulado)
@Composable
private fun Detail(label: String, value: String) {
    Column {
        Text(label, fontSize = 10.sp, color = HomeColors.Subtitle)
        Spacer(Modifier.height(2.dp))
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = HomeColors.Title)
    }
}

// preview con las tres solicitudes de ejemplo
@Preview(showBackground = true)
@Composable
private fun ApplicatorHomePreview() {
    TecMundoTheme { ApplicatorHomeScreen(userName = "Diego Soto") }
}
