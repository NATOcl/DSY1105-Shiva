package cl.duoc.tecmundo.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.tecmundo.R
import cl.duoc.tecmundo.model.Parcel
import cl.duoc.tecmundo.ui.ShivaTextField
import kotlinx.coroutines.launch

// ventana "Nueva solicitud": sube desde abajo al tocar "Solicitar aplicacion" en una parcela
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRequestSheet(
    parcel: Parcel,              // la parcela para la que se pide la aplicacion
    onDismiss: () -> Unit,       // se cerro sin publicar
    onPublish: () -> Unit        // se toco "Publicar solicitud" y todo estaba bien
) {
    // lo que el usuario va eligiendo o escribiendo (rememberSaveable lo conserva si gira el telefono)
    var product by rememberSaveable { mutableStateOf("Granulado") }
    var hectares by rememberSaveable { mutableStateOf("") }
    var schedule by rememberSaveable { mutableStateOf("Diurna") }
    var hasWater by rememberSaveable { mutableStateOf(true) }
    var comments by rememberSaveable { mutableStateOf("") }
    // el error de la superficie solo se muestra despues de que el usuario intento publicar
    var touched by rememberSaveable { mutableStateOf(false) }

    // revisa la superficie: tiene que ser un numero, mayor que 0 y sin pasar las hectareas de la parcela
    val number = hectares.toIntOrNull()
    val hectaresError = when {
        number == null -> "Ingresa la superficie a aplicar"
        number <= 0 -> "La superficie debe ser mayor que 0"
        number > parcel.hectares -> "La parcela solo tiene ${parcel.hectares} ha"
        else -> null
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    // primero se baja la ventana con animacion y despues se hace lo que corresponda
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
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 16.dp)
        ) {
            // nombre de la parcela, titulo y boton para cerrar
            Row(verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(parcel.name.uppercase(), color = HomeColors.Eyebrow, fontWeight = FontWeight.Bold,
                        fontSize = 12.sp, letterSpacing = 1.5.sp)
                    Spacer(Modifier.height(6.dp))
                    Text("Nueva solicitud", color = HomeColors.Title,
                        fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                }
                Box(
                    Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFFFF1BF))
                        .clickable { closeThen(onDismiss) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painterResource(R.drawable.ic_close), "Cerrar",
                        tint = HomeColors.Title, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(Modifier.height(20.dp))

            // cada RequestChoiceRow es una fila de opciones (mas abajo en este mismo archivo)
            RequestChoiceRow("TIPO DE PRODUCTO", listOf("Granulado", "Líquido"), product) { product = it }

            // superficie: ShivaTextField es el mismo campo que usas en NewParcelScreen
            ShivaTextField(
                "SUPERFICIE A APLICAR",
                hectares,
                "Hasta ${parcel.hectares} ha",              // el maximo es la superficie de la parcela
                if (touched) hectaresError else null,
                { hectares = it.filter(Char::isDigit).take(5) },   // solo deja escribir numeros
                { touched = true },
                KeyboardType.Number
            )

            RequestChoiceRow("HORARIO DE APLICACIÓN", listOf("Diurna", "Nocturna"), schedule) { schedule = it }
            RequestChoiceRow("¿CUENTA CON AGUA?", listOf("Sí", "No"), if (hasWater) "Sí" else "No") {
                hasWater = it == "Sí"
            }

            ShivaTextField(
                "OBSTÁCULOS O COMENTARIOS", comments, "Cables, antenas, árboles...", null,
                { comments = it.take(120) }, {}, KeyboardType.Text
            )

            Spacer(Modifier.height(16.dp))
            // boton publicar
            Row(
                Modifier.fillMaxWidth().height(56.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(HomeColors.Title)
                    .clickable {
                        touched = true                                  // ahora si se muestran los errores
                        if (hectaresError == null) closeThen(onPublish) // solo publica si esta todo bien
                    }
                    .padding(start = 20.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Publicar solicitud", color = Color.White, fontWeight = FontWeight.Bold,
                    fontSize = 15.sp, modifier = Modifier.weight(1f))
                Box(
                    Modifier.size(36.dp).clip(CircleShape).background(ShivaColors.Yellow),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painterResource(R.drawable.ic_arrow_right), null, tint = ShivaColors.BrownDark)
                }
            }
        }
    }
}

// fila de opciones para elegir una (ej. Granulado / Liquido)
@Composable
private fun RequestChoiceRow(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold,
        color = ShivaColors.BrownText, letterSpacing = 1.sp)
    Spacer(Modifier.height(8.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            val activa = option == selected
            Box(
                Modifier.weight(1f).height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    // la opcion elegida va amarilla suave con borde amarillo; las otras blancas
                    .background(if (activa) Color(0xFFFFF1BF) else Color.White)
                    .border(1.dp, if (activa) ShivaColors.Yellow else ShivaColors.Border, RoundedCornerShape(14.dp))
                    .clickable { onSelect(option) },
                contentAlignment = Alignment.Center
            ) {
                Text(option, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = HomeColors.Title)
            }
        }
    }
    Spacer(Modifier.height(16.dp))
}
