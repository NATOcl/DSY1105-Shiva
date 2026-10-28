package cl.duoc.tecmundo.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.tecmundo.R
import cl.duoc.tecmundo.model.ProviderCatalog
import cl.duoc.tecmundo.ui.FieldLabel
import cl.duoc.tecmundo.ui.ShivaTextField
import cl.duoc.tecmundo.viewmodel.ProviderField
import cl.duoc.tecmundo.viewmodel.ProviderProfileViewModel

// formulario del perfil del prestador: lo que ofrece, donde cubre y cuanto puede aplicar
// es la informacion que despues usa el matching para proponerle solicitudes
@Composable
fun ProviderProfileScreen(
    ownerRut: String,              // rut del aplicador dueño del perfil
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: ProviderProfileViewModel = viewModel()
) {
    val ui by viewModel.state.collectAsStateWithLifecycle()

    // al abrir la pantalla se carga el perfil guardado (si ya tenia uno)
    LaunchedEffect(ownerRut) { viewModel.load(ownerRut) }

    Box(
        Modifier.fillMaxSize().background(
            Brush.linearGradient(listOf(Color(0xFFFFF1C2), Color.White, ShivaColors.CreamSoft))
        )
    ) {
        Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().imePadding()) {

            // boton volver
            Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                Box(
                    Modifier.size(40.dp).clip(CircleShape).background(Color.White)
                        .clickable(onClick = onBack).align(Alignment.CenterStart),
                    contentAlignment = Alignment.Center
                ) { Icon(painterResource(R.drawable.ic_back), "Volver", tint = ShivaColors.BrownDark) }
            }

            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp)) {
                Text("Perfil de prestador", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold,
                    color = ShivaColors.BrownDark, letterSpacing = (-0.5).sp)
                Spacer(Modifier.height(8.dp))
                Text("Cuéntale a los productores qué servicios ofreces y dónde puedes trabajar.",
                    fontSize = 14.sp, color = Color(0xFF6B5B2E), lineHeight = 20.sp)
                Spacer(Modifier.height(24.dp))

                ChipGroup("SERVICIOS QUE OFRECES", ProviderCatalog.services, ui.services,
                    ui.servicesError, viewModel::onServiceToggle)
                ChipGroup("PRODUCTOS QUE APLICAS", ProviderCatalog.products, ui.products,
                    ui.productsError, viewModel::onProductToggle)
                ChipGroup("HORARIO DE TRABAJO", ProviderCatalog.schedules, ui.schedules,
                    ui.schedulesError, viewModel::onScheduleToggle)

                ShivaTextField("CULTIVOS CON EXPERIENCIA", ui.crops, "Ej. Maíz, Trigo, Vid", ui.cropsError,
                    viewModel::onCropsChange, { viewModel.onFieldBlur(ProviderField.CROPS) }, KeyboardType.Text)
                ShivaTextField("ZONAS DE COBERTURA", ui.coverage, "Ej. Valle de Colchagua, Maule", ui.coverageError,
                    viewModel::onCoverageChange, { viewModel.onFieldBlur(ProviderField.COVERAGE) }, KeyboardType.Text)
                ShivaTextField("CAPACIDAD (HECTÁREAS POR DÍA)", ui.capacity, "Ej. 40", ui.capacityError,
                    viewModel::onCapacityChange, { viewModel.onFieldBlur(ProviderField.CAPACITY) }, KeyboardType.Number)
                ShivaTextField("AÑOS DE EXPERIENCIA", ui.experience, "Ej. 5", ui.experienceError,
                    viewModel::onExperienceChange, { viewModel.onFieldBlur(ProviderField.EXPERIENCE) }, KeyboardType.Number)

                // interruptor de disponibilidad
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Disponible ahora", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = HomeColors.Title)
                        Text("Si lo apagas, no te propondremos nuevas solicitudes.",
                            fontSize = 12.sp, color = HomeColors.Subtitle)
                    }
                    Switch(
                        checked = ui.available, onCheckedChange = viewModel::onAvailableChange,
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = ShivaColors.Brown, checkedThumbColor = ShivaColors.Yellow
                        )
                    )
                }

                Spacer(Modifier.height(20.dp))
                // boton guardar
                Row(
                    Modifier.fillMaxWidth().height(56.dp)
                        .shadow(8.dp, RoundedCornerShape(18.dp))
                        .clip(RoundedCornerShape(18.dp))
                        .background(ShivaColors.Brown)
                        .clickable { viewModel.onSubmit(ownerRut, onSaved) }
                        .padding(start = 20.dp, end = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Guardar perfil", color = Color.White, fontWeight = FontWeight.Bold,
                        fontSize = 15.sp, modifier = Modifier.weight(1f))
                    Box(Modifier.size(36.dp).clip(CircleShape).background(ShivaColors.Yellow),
                        contentAlignment = Alignment.Center) {
                        Icon(painterResource(R.drawable.ic_arrow_right), null, tint = ShivaColors.BrownDark)
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

// grupo de opciones tipo pildora, se pueden marcar varias (de a dos por fila para que no se corten)
@Composable
private fun ChipGroup(
    label: String,
    options: List<String>,
    selected: Set<String>,
    error: String?,
    onToggle: (String) -> Unit
) {
    FieldLabel(label)
    options.chunked(2).forEach { rowOptions ->
        Row(Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            rowOptions.forEach { option ->
                val on = option in selected
                Box(
                    Modifier.weight(1f).clip(RoundedCornerShape(50))
                        .background(if (on) ShivaColors.Yellow else Color.White)
                        .clickable { onToggle(option) }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(option, fontSize = 13.sp,
                        fontWeight = if (on) FontWeight.Bold else FontWeight.Medium,
                        color = ShivaColors.BrownDark)
                }
            }
            // si la fila queda con una sola opcion, rellena el espacio para que mida igual
            if (rowOptions.size == 1) Spacer(Modifier.weight(1f))
        }
    }
    if (error != null) {
        Text(error, fontSize = 12.sp, color = ShivaColors.Error)
    }
    Spacer(Modifier.height(12.dp))
}
