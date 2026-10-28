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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import android.Manifest
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

// pantalla para registrar una parcela nueva (la del productor con sesion abierta)
@Composable
fun NewParcelScreen(
    ownerRut: String,                 // rut del productor, asi la parcela queda ligada a su cuenta
    onBack: () -> Unit,               // volver a la lista sin guardar
    onSaved: () -> Unit,              // que pasa cuando la parcela ya quedo guardada
    viewModel: NewParcelViewModel = viewModel()
) {
    // estado actual del formulario
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    // ---------- CÁMARA ----------
    val context = LocalContext.current
    var archivoPendiente by remember { mutableStateOf<File?>(null) }

    // 1) lanzador de la cámara: cuando se saca la foto, guardamos su ruta
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        val file = archivoPendiente
        if (success && file != null) viewModel.onPhotoChange(file.absolutePath)
    }

    // crea el archivo donde se guarda la foto y abre la cámara
    val abrirCamara = {
        val carpeta = File(context.filesDir, "parcelas").apply { mkdirs() }
        val file = File.createTempFile("parcela_", ".jpg", carpeta)
        archivoPendiente = file
        val uri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)
        takePictureLauncher.launch(uri)
    }

    //  lanzador del permiso: si el usuario acepta, abre la cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) abrirCamara() }

    // convierte la ruta guardada en una imagen para mostrarla
    val foto = remember(uiState.photoPath) {
        uiState.photoPath?.let {
            BitmapFactory.decodeFile(it, BitmapFactory.Options().apply { inSampleSize = 4 })
                ?.asImageBitmap()
        }
    }
    // ---------- FIN CÁMARA ----------

    // cuando se guarda: limpiamos el formulario y avisamos para volver a la lista
    LaunchedEffect(uiState.saved) {
        if (uiState.saved) {
            viewModel.reset()
            onSaved()
        }
    }

    // fondo con degradado suave color crema, igual que el registro
    Box(
        Modifier.fillMaxSize().background(
            Brush.linearGradient(listOf(Color(0xFFFFF1C2), Color.White, ShivaColors.CreamSoft))
        )
    ) {
        // imePadding sube el contenido cuando aparece el teclado
        Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().imePadding()) {

            // barra de arriba: boton volver y logo
            Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                Box(
                    Modifier.size(40.dp).clip(CircleShape).background(Color.White)
                        .clickable(onClick = onBack)
                        .align(Alignment.CenterStart),
                    contentAlignment = Alignment.Center
                ) { Icon(painterResource(R.drawable.ic_back), "Volver", tint = ShivaColors.BrownDark) }
                Row(Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.ic_logo), null, tint = ShivaColors.Brown, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Shiva", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = ShivaColors.BrownDark)
                }
            }

            // zona con scroll donde va todo el formulario
            Column(
                Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp)
            ) {
                // titulos de la pantalla
                Text("MIS PARCELAS", fontSize = 12.sp, fontWeight = FontWeight.Bold,
                    color = ShivaColors.BrownText, letterSpacing = 1.sp)
                Spacer(Modifier.height(4.dp))
                Text("Nueva parcela", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold,
                    color = ShivaColors.BrownDark, letterSpacing = (-0.5).sp)
                Spacer(Modifier.height(8.dp))
                Text("Registra el cultivo y la superficie para poder pedir una aplicación.",
                    fontSize = 14.sp, color = Color(0xFF6B5B2E), lineHeight = 20.sp)

                Spacer(Modifier.height(24.dp))
                // campos del formulario, cada uno con su propia validacion

                // recuadro de la foto del terreno (NUEVO)
                Text("FOTO DEL TERRENO", fontSize = 12.sp, fontWeight = FontWeight.Bold,
                    color = ShivaColors.BrownText, letterSpacing = 1.sp)
                Spacer(Modifier.height(8.dp))
                Box(
                    Modifier.fillMaxWidth().height(160.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFFFF7DB))
                        .clickable { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                    contentAlignment = Alignment.Center
                ) {
                    if (foto != null) {
                        Image(foto, "Foto del terreno", Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop)
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(Modifier.size(40.dp).clip(CircleShape).background(ShivaColors.Yellow),
                                contentAlignment = Alignment.Center) { Text("📷") }
                            Spacer(Modifier.height(8.dp))
                            Text("Tomar una foto", fontWeight = FontWeight.Bold,
                                color = ShivaColors.Brown)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))

                ShivaTextField("NOMBRE DE LA PARCELA", uiState.name, "Ej. Parcela 01",
                    uiState.nameError, viewModel::onNameChange, { viewModel.onFieldBlur(ParcelField.NAME) },
                    KeyboardType.Text)
                ShivaTextField("ZONA", uiState.zone, "Ej. Valle Central",
                    uiState.zoneError, viewModel::onZoneChange, { viewModel.onFieldBlur(ParcelField.ZONE) },
                    KeyboardType.Text)
                ShivaTextField("CULTIVO", uiState.crop, "Ej. Maíz",
                    uiState.cropError, viewModel::onCropChange, { viewModel.onFieldBlur(ParcelField.CROP) },
                    KeyboardType.Text)
                ShivaTextField("SUPERFICIE (HECTÁREAS)", uiState.hectares, "Ej. 10",
                    uiState.hectaresError, viewModel::onHectaresChange, { viewModel.onFieldBlur(ParcelField.HECTARES) },
                    KeyboardType.Number)

                Spacer(Modifier.height(8.dp))

                // boton guardar: se apaga mientras se esta guardando
                val enabled = !uiState.saving
                Row(
                    Modifier.fillMaxWidth().height(56.dp)
                        .shadow(if (enabled) 8.dp else 0.dp, RoundedCornerShape(18.dp))
                        .clip(RoundedCornerShape(18.dp))
                        .background(ShivaColors.Brown)
                        .alpha(if (enabled) 1f else 0.45f)
                        .clickable(enabled = enabled) { viewModel.onSubmit(ownerRut) }
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