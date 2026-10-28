package cl.duoc.tecmundo.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import cl.duoc.tecmundo.R
import cl.duoc.tecmundo.model.ApplicationRequest
import cl.duoc.tecmundo.ui.theme.ShivaColors
import cl.duoc.tecmundo.ui.theme.TecMundoTheme

// Datos de prueba para poder ver la pantalla mientras todavía no cargamos
// las solicitudes reales desde el backend o la base de datos.
private val sampleRequests = listOf(
    ApplicationRequest(1, "Parcela El Alerece", 12, "PENDIENTE", "Granulado", 10, "Diurna"),
    ApplicationRequest(2, "Parcela Los Aromos", 20, "PENDIENTE", "Líquido", 14, "Nocturna"),
    ApplicationRequest(3, "Campo Vista Sur", 27, "PENDIENTE", "Líquido", 8, "Diurna")
)

// Esta es la pantalla principal del aplicador.
// Recibe el nombre del usuario, las solicitudes que debe mostrar y las acciones
// que la aplicación debe ejecutar al abrir una parcela o cerrar la sesión.
@Composable
fun ApplicatorHomeScreen(
    userName: String,
    requests: List<ApplicationRequest> = sampleRequests,
    onRequestClick: (ApplicationRequest) -> Unit = {},
    onLogOut: () -> Unit = {}
) {
    // Guarda la parcela seleccionada. Si vale null, no hay detalle abierto.
    var selectedRequest by remember {
        mutableStateOf<ApplicationRequest?>(null)
    }

    // Controla si se muestra el mensaje verde de confirmación.
    // Al principio está en false, por lo que el mensaje no aparece.
    var sentMessage by remember {
        mutableStateOf(false)
    }

    // Cuando el mensaje verde aparece, esperamos 3 segundos y lo ocultamos.
    LaunchedEffect(sentMessage) {
        if (sentMessage) {
            delay(3000)
            sentMessage = false
        }
    }

    // HomeBackground aplica el fondo y el estilo general de la aplicación.
    HomeBackground {
        // La cabecera muestra el avatar y el nombre del usuario.
        // Al tocar el avatar, se ejecuta onLogOut.
        HomeHeader(
            userName = userName,
            greeting = "BIENVENIDA",
            onAvatarClick = onLogOut
        )

        Spacer(Modifier.height(20.dp))

        // Título y texto que explican qué puede hacer el aplicador.
        Text(
            "OPORTUNIDADES CERCANAS",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            color = HomeColors.Eyebrow
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Revisa solicitudes cercanas y envía tu oferta al productor.",
            fontSize = 15.sp,
            color = HomeColors.Subtitle,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(12.dp))

        // El contador se calcula con la cantidad de solicitudes recibidas.
        Box(
            Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            CountPill("${requests.size} disponibles")
        }

        Spacer(Modifier.height(12.dp))

        // LazyColumn dibuja las parcelas en una lista desplazable.
        // Cada tarjeta usa el ID como clave para identificarla.
        LazyColumn(
            modifier = Modifier.weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(requests, key = { it.id }) { request ->
                RequestCard(request) {
                    // Guardamos la parcela seleccionada para abrir su detalle.
                    selectedRequest = request

                    // Si había un mensaje de confirmación, lo ocultamos.
                    sentMessage = false

                    // Avisamos a la pantalla que contiene esta vista.
                    onRequestClick(request)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Este bloque solo se dibuja cuando sentMessage es true.
        if (sentMessage) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF104432))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(ShivaColors.Yellow),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "✓",
                        color = ShivaColors.BrownDark,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(10.dp))

                Column {
                    Text(
                        "Datos guardados",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        "Tu oferta fue enviada al productor.",
                        color = Color(0xFFC7D9D0),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }

    // Si hay una parcela seleccionada, abrimos el diálogo con sus datos.
    // Cuando selectedRequest vuelve a null, el diálogo desaparece.
    selectedRequest?.let { request ->
        OfferDetailsDialog(
            request = request,
            onDismiss = {
                // Cierra la ventana sin enviar la oferta.
                selectedRequest = null
            },
            onSend = {
                // Se ejecuta al pulsar "Enviar oferta".
                // Cerramos el detalle y activamos el mensaje verde.
                selectedRequest = null
                sentMessage = true
            }
        )
    }
}

// Dibuja una tarjeta resumida por cada solicitud de parcela.
@Composable
private fun RequestCard(
    request: ApplicationRequest,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(166.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
    ) {
        // Usa la foto asignada a la solicitud o la imagen de trigo por defecto.
        Image(
            painter = painterResource(request.imageRes ?: R.drawable.bg_wheat),
            contentDescription = request.farmName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(78.dp)
                .fillMaxHeight()
        )

        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    // Muestra la distancia y el estado de la solicitud.
                    Text(
                        "A ${request.distanceKm} KM · ${request.status}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = HomeColors.Pending
                    )

                    Spacer(Modifier.height(5.dp))

                    // Nombre de la parcela publicado por el productor.
                    Text(
                        request.farmName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = HomeColors.Title
                    )
                }

                // Flecha decorativa que indica que se puede abrir el detalle.
                Box(
                    Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(ShivaColors.Yellow),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_right),
                        contentDescription = "Ver solicitud",
                        tint = ShivaColors.BrownDark,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Por ahora el cultivo es un ejemplo fijo según el ID.
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFF9DF))
                    .padding(horizontal = 10.dp, vertical = 7.dp)
            ) {
                Text(
                    "DESCRIPCIÓN",
                    fontSize = 8.sp,
                    color = HomeColors.Subtitle,
                    letterSpacing = 0.5.sp
                )
                Text(
                    "Tipo de cultivo: ${if (request.id == 2) "Arroz" else "Maíz amarillo"}",
                    fontSize = 10.sp,
                    color = HomeColors.Title,
                    fontWeight = FontWeight.Medium
                )
            }

            // Deja el espacio sobrante entre la descripción y los datos de abajo.
            Spacer(Modifier.weight(1f))

            // Resumen de los datos principales de la solicitud.
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Detail("Producto", request.product, Modifier.weight(1f))
                Detail("Superficie", "${request.hectares} ha", Modifier.weight(1f))
                Detail("Aplicación", request.schedule, Modifier.weight(1f))
            }
        }
    }
}

// Componente reutilizable que muestra una etiqueta y su valor.
@Composable
private fun Detail(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier.widthIn(min = 0.dp)) {
        Text(
            label,
            fontSize = 9.sp,
            color = HomeColors.Subtitle,
            maxLines = 1
        )
        Spacer(Modifier.height(2.dp))
        Text(
            value,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = HomeColors.Title,
            maxLines = 1
        )
    }
}

// Ventana que aparece al seleccionar una parcela.
// Aquí el aplicador revisa la información y escribe cuánto cobra por hectárea.
@Composable
private fun OfferDetailsDialog(
    request: ApplicationRequest,
    onDismiss: () -> Unit,
    onSend: () -> Unit
) {
    // El precio se guarda en el estado local mientras se escribe.
    // Si se abre otra parcela, el campo se reinicia.
    var price by remember(request.id) {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFFFFDF5),
        shape = RoundedCornerShape(24.dp),
        title = {
            // Encabezado del detalle: distancia y nombre de la parcela.
            Column {
                Text(
                    "TRABAJO A ${request.distanceKm} KM",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.4.sp,
                    color = HomeColors.Eyebrow
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    request.farmName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = HomeColors.Title
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Datos de quien publicó la solicitud. El nombre es genérico por ahora.
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .padding(12.dp)
                ) {
                    Text(
                        "SOLICITUD PUBLICADA POR",
                        fontSize = 9.sp,
                        color = HomeColors.Subtitle,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Productor de la parcela",
                        fontSize = 13.sp,
                        color = HomeColors.Title,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Dos filas de recuadros con los datos de la parcela.
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoBox("Producto", request.product, Modifier.weight(1f))
                    InfoBox("Superficie", "${request.hectares} ha", Modifier.weight(1f))
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoBox("Aplicación", request.schedule, Modifier.weight(1f))
                    InfoBox("Tiene agua", "Sí", Modifier.weight(1f))
                }

                // Este dato es demostrativo; debería venir de la solicitud real.
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFFFF6D9))
                        .padding(12.dp)
                ) {
                    Text(
                        "OBSTÁCULOS INFORMADOS",
                        fontSize = 9.sp,
                        color = HomeColors.Subtitle,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        "Árboles nativos en deslinde norte",
                        fontSize = 11.sp,
                        color = HomeColors.Title
                    )
                }

                Text(
                    "TU PRECIO POR HECTÁREA",
                    fontSize = 10.sp,
                    color = HomeColors.Subtitle,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )

                // Campo de precio: solo permite dígitos y limita la cantidad a 9 caracteres.
                OutlinedTextField(
                    value = price,
                    onValueChange = { input ->
                        price = input.filter { it.isDigit() }.take(9)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("$ 00.000 CLP", color = Color(0xFF9AA69F)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp)
                )
            }
        },
        confirmButton = {
            // El botón se habilita solo si el campo contiene un número válido.
            Button(
                onClick = onSend,
                enabled = price.isNotBlank() && price.toLongOrNull() != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF104432),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Enviar oferta", fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Text("›", fontSize = 24.sp, color = ShivaColors.Yellow)
            }
        },
        dismissButton = {
            // Este botón solo cierra el detalle; no envía nada.
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = HomeColors.Title)
            }
        }
    )
}

// Recuadro reutilizable para presentar un dato del detalle.
@Composable
private fun InfoBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(12.dp)
    ) {
        Text(
            label,
            fontSize = 9.sp,
            color = HomeColors.Subtitle,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            value,
            fontSize = 12.sp,
            color = HomeColors.Title,
            fontWeight = FontWeight.Bold
        )
    }
}

// Vista previa para revisar el diseño en Android Studio sin iniciar sesión.
@Preview(showBackground = true)
@Composable
private fun ApplicatorHomePreview() {
    TecMundoTheme {
        ApplicatorHomeScreen(userName = "Diego Soto")
    }
}
