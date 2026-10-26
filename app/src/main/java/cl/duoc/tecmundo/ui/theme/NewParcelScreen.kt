package cl.duoc.tecmundo.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import cl.duoc.tecmundo.ui.ShivaTextField
import cl.duoc.tecmundo.viewmodel.NewParcelViewModel
import cl.duoc.tecmundo.viewmodel.ParcelField

// formulario para registrar una parcela nueva
@Composable
fun NewParcelScreen(
    ownerRut: String,              // rut del productor que la registra
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: NewParcelViewModel = viewModel()
) {
    val ui by viewModel.state.collectAsStateWithLifecycle()

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
                Text("Nueva parcela", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold,
                    color = ShivaColors.BrownDark, letterSpacing = (-0.5).sp)
                Spacer(Modifier.height(8.dp))
                Text("Registra los datos de tu campo para poder solicitar aplicaciones.",
                    fontSize = 14.sp, color = Color(0xFF6B5B2E), lineHeight = 20.sp)
                Spacer(Modifier.height(24.dp))

                ShivaTextField("NOMBRE DE LA PARCELA", ui.name, "Ej. Parcela 01", ui.nameError,
                    viewModel::onNameChange, { viewModel.onFieldBlur(ParcelField.NAME) }, KeyboardType.Text)
                ShivaTextField("CULTIVO", ui.crop, "Ej. Maíz", ui.cropError,
                    viewModel::onCropChange, { viewModel.onFieldBlur(ParcelField.CROP) }, KeyboardType.Text)
                ShivaTextField("SUPERFICIE (HECTÁREAS)", ui.hectares, "Ej. 25", ui.hectaresError,
                    viewModel::onHectaresChange, { viewModel.onFieldBlur(ParcelField.HECTARES) }, KeyboardType.Number)
                ShivaTextField("ZONA", ui.zone, "Ej. Valle de Colchagua", ui.zoneError,
                    viewModel::onZoneChange, { viewModel.onFieldBlur(ParcelField.ZONE) }, KeyboardType.Text)

                Spacer(Modifier.height(16.dp))
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
                    Text("Guardar parcela", color = Color.White, fontWeight = FontWeight.Bold,
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