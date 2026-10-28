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

// Estos datos nos sirven para probar la pantalla mientras conectamos las solicitudes reales.
private val sampleRequests = listOf(
    ApplicationRequest(1, "Parcela El Alerece", 12, "PENDIENTE", "Granulado", 10, "Diurna"),
    ApplicationRequest(2, "Parcela Los Aromos", 20, "PENDIENTE", "Líquido", 14, "Nocturna"),
    ApplicationRequest(3, "Campo Vista Sur", 27, "PENDIENTE", "Líquido", 8, "Diurna")
)

// Esta es la pantalla que ve el aplicador al entrar.
// Recibe su nombre, las solicitudes y las acciones que se ejecutan al tocar una parcela o cerrar sesion.
@Composable
fun ApplicatorHomeScreen(
    userName: String,
    requests: List<ApplicationRequest> = sampleRequests,
    onRequestClick: (ApplicationRequest) -> Unit = {},
    hasProfile: Boolean = true,          // false si todavia no completa su perfil de prestador
    onEditProfile: () -> Unit = {},      // abre el formulario del perfil de prestador
    onLogOut: () -> Unit = {}
) {
    // Aqui guardamos la parcela que eligio el usuario. Si queda en null, no hay ninguna abierta.
    var selectedRequest by remember {
        mutableStateOf<ApplicationRequest?>(null)
    }

    // Esta variable decide si mostramos el aviso verde. Parte en false, asi que al entrar no se ve.
    var sentMessage by remember {
        mutableStateOf(false)
    }

    // Cuando aparece el aviso verde, esperamos 3 segundos y despues lo escondemos.
    LaunchedEffect(sentMessage) {
        if (sentMessage) {
            delay(3000)
            sentMessage = false
        }
    }

    // El avatar abre el menu lateral del perfil, donde esta el boton para cerrar sesion.
    ProfileDrawerHost(
        userName = userName, role = "Aplicador", onLogOut = onLogOut, onProfile = onEditProfile
    ) { openDrawer ->
        // Este componente pone el fondo y mantiene el estilo visual de la app.
        HomeBackground {
            // Arriba se muestra el nombre y el avatar. Al tocarlo, se abre el menu lateral.
            HomeHeader(
                userName = userName,
                greeting = "BIENVENIDA",
                onAvatarClick = openDrawer
            )

            Spacer(Modifier.height(20.dp))

            // Aviso para completar el perfil de prestador: sin el, los productores no pueden encontrarlo.
            if (!hasProfile) {
                Row(
                    Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFFFF7DB))
                        .clickable(onClick = onEditProfile)
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Completa tu perfil de prestador", fontSize = 14.sp,
                            fontWeight = FontWeight.Bold, color = HomeColors.Title)
                        Text("Agrega tus servicios y cobertura para que te encuentren.",
                            fontSize = 12.sp, color = ShivaColors.BrownText)
                    }
                    Text("›", fontSize = 24.sp, color = ShivaColors.BrownDark)
                }
                Spacer(Modifier.height(16.dp))
            }

            // Este titulo y el texto de abajo explican que puede hacer el aplicador.
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

            // Aqui mostramos cuantas solicitudes hay disponibles.
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                CountPill("${requests.size} disponibles")
            }

            Spacer(Modifier.height(12.dp))

            // LazyColumn muestra las parcelas en una lista que se puede deslizar.
            // Usamos el ID para que Compose reconozca cada tarjeta.
            LazyColumn(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(requests, key = { it.id }) { request ->
                    RequestCard(request) {
                        // Guardamos la parcela que se toco para mostrar su informacion.
                        selectedRequest = request

                        // Si quedo un aviso anterior en pantalla, lo ocultamos.
                        sentMessage = false

                        // Tambien avisamos a la pantalla principal que se eligio esta solicitud.
                        onRequestClick(request)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // El aviso verde aparece solamente cuando sentMessage vale true.
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
    }

    // Si se eligio una parcela, abrimos la ventana con sus datos.
    // Al volver selectedRequest a null, la ventana se cierra.
    selectedRequest?.let { request ->
        OfferDetailsDialog(
            request = request,
            onDismiss = {
                // Cierra la ventana sin enviar la oferta.
                selectedRequest = null
            },
            onSend = {
                // Esto pasa al tocar "Enviar oferta": cerramos la ventana y mostramos el aviso verde.
                selectedRequest = null
                sentMessage = true
            }
        )
    }
}

// Esta funcion arma la tarjeta resumida de cada parcela.
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
        // Si la solicitud tiene foto, la usamos; si no, mostramos la imagen de trigo.
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
                    // Aqui se ve a cuantos kilometros esta la parcela y en que estado va la solicitud.
                    Text(
                        "A ${request.distanceKm} KM · ${request.status}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = HomeColors.Pending
                    )

                    Spacer(Modifier.height(5.dp))

                    // Mostramos el nombre de la parcela que publico el productor.
                    Text(
                        request.farmName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = HomeColors.Title
                    )
                }

                // La flecha da una pista de que se puede tocar la tarjeta para ver mas detalles.
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

            // Por ahora mostramos un cultivo de ejemplo segun el ID de la solicitud.
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

            // Este espacio ayuda a mantener los datos de abajo alineados.
            Spacer(Modifier.weight(1f))

            // Aqui dejamos a la vista los datos principales de la solicitud.
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

// Esta funcion nos evita repetir el mismo formato para cada dato.
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
// Aqui el aplicador revisa la informacion y escribe cuanto cobra por hectarea.
@Composable
private fun OfferDetailsDialog(
    request: ApplicationRequest,
    onDismiss: () -> Unit,
    onSend: () -> Unit
) {
    // Guardamos el precio mientras se escribe. Si se abre otra parcela, el campo parte vacio.
    var price by remember(request.id) {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFFFFDF5),
        shape = RoundedCornerShape(24.dp),
        title = {
            // En esta parte mostramos la distancia y el nombre de la parcela elegida.
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
                // Aqui aparece quien publico la solicitud; por ahora usamos un nombre generico.
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

                // Estos recuadros ordenan los datos de la parcela para que se lean facil.
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoBox("Producto", request.product, Modifier.weight(1f))
                    InfoBox("Superficie", "${request.hectares} ha", Modifier.weight(1f))
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoBox("Aplicación", request.schedule, Modifier.weight(1f))
                    InfoBox("Tiene agua", "Sí", Modifier.weight(1f))
                }

                // Este dato es solo de ejemplo; mas adelante deberia venir de la solicitud real.
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

                // En este campo solo aceptamos numeros y dejamos escribir hasta 9 digitos.
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
            // El boton se activa cuando se escribe un precio valido.
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
            // Este boton cierra la ventana sin enviar la oferta.
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = HomeColors.Title)
            }
        }
    )
}

// Este recuadro sirve para mostrar cada dato con el mismo estilo.
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

// Esta vista previa permite revisar el diseno sin tener que iniciar sesion.
@Preview(showBackground = true)
@Composable
private fun ApplicatorHomePreview() {
    TecMundoTheme {
        ApplicatorHomeScreen(userName = "Diego Soto")
    }
}
