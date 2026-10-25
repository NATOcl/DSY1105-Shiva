package cl.duoc.tecmundo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.tecmundo.R
import cl.duoc.tecmundo.model.ProfileType
import cl.duoc.tecmundo.ui.theme.ShivaColors
import cl.duoc.tecmundo.viewmodel.Field
import cl.duoc.tecmundo.viewmodel.RegisterViewModel

// pantalla de registro: formulario con validacion en vivo
@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegistered: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    // estado actual del formulario
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    // dialogo de exito cuando la cuenta ya se guardo
    if (uiState.registrationDone) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Cuenta creada") },
            text = { Text("Tu cuenta quedó registrada en este dispositivo.") },
            confirmButton = {
                TextButton(onClick = { viewModel.onRegistrationHandled(); onRegistered() }) { Text("Continuar") }
            }
        )
    }

    // fondo con degradado suave color crema
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
                // barrita de progreso del paso 1 de 2
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(Modifier.width(28.dp).height(6.dp).clip(CircleShape).background(ShivaColors.Brown))
                    Box(Modifier.width(10.dp).height(6.dp).clip(CircleShape).background(ShivaColors.Yellow))
                }
                Spacer(Modifier.height(10.dp))
                // titulos de la pantalla
                Text("PASO 1 DE 2", fontSize = 12.sp, fontWeight = FontWeight.Bold,
                    color = ShivaColors.BrownText, letterSpacing = 1.sp)
                Spacer(Modifier.height(4.dp))
                Text("Crea tu cuenta", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold,
                    color = ShivaColors.BrownDark, letterSpacing = (-0.5).sp)
                Spacer(Modifier.height(8.dp))
                Text("Empecemos con tus datos para preparar tu espacio de trabajo.",
                    fontSize = 14.sp, color = Color(0xFF6B5B2E), lineHeight = 20.sp)

                // aviso si ya hay una cuenta registrada en este telefono
                if (uiState.accountAlreadyExists && !uiState.registrationDone) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Ya existe una cuenta registrada en este dispositivo.",
                        color = ShivaColors.Error, fontSize = 13.sp, fontWeight = FontWeight.Medium
                    )
                }

                Spacer(Modifier.height(24.dp))
                // selector de perfil: productor o aplicador
                FieldLabel("¿CUÁL ES TU PERFIL?")
                ProfileSelector(uiState.profile, viewModel::onProfileChange)

                Spacer(Modifier.height(20.dp))
                // campos del formulario, cada uno con su propia validacion
                ShivaTextField("NOMBRE COMPLETO", uiState.fullName, "Ej. María González",
                    uiState.nameError, viewModel::onNameChange, { viewModel.onFieldBlur(Field.NAME) },
                    KeyboardType.Text)
                ShivaTextField("RUT", uiState.rut, "12.345.678-9",
                    uiState.rutError, viewModel::onRutChange, { viewModel.onFieldBlur(Field.RUT) },
                    KeyboardType.Ascii)
                ShivaTextField("CORREO ELECTRÓNICO", uiState.email, "nombre@correo.com",
                    uiState.emailError, viewModel::onEmailChange, { viewModel.onFieldBlur(Field.EMAIL) },
                    KeyboardType.Email)
                ShivaTextField("TELÉFONO", uiState.phone, "+56 9 1234 5678",
                    uiState.phoneError, viewModel::onPhoneChange, { viewModel.onFieldBlur(Field.PHONE) },
                    KeyboardType.Phone)
                ShivaTextField("NOMBRE DE TU FINCA", uiState.farmName, "Ej. Finca El Porvenir",
                    uiState.farmError, viewModel::onFarmChange, { viewModel.onFieldBlur(Field.FARM) },
                    KeyboardType.Text)
                ShivaTextField("CONTRASEÑA", uiState.password, "Mínimo 8 caracteres",
                    uiState.passwordError, viewModel::onPasswordChange, { viewModel.onFieldBlur(Field.PASSWORD) },
                    KeyboardType.Password,
                    isPassword = true, passwordVisible = uiState.passwordVisible,
                    onTogglePassword = viewModel::togglePasswordVisible)

                // checkbox de terminos y condiciones
                Row(verticalAlignment = Alignment.Top) {
                    Checkbox(
                        checked = uiState.termsAccepted,
                        onCheckedChange = viewModel::onTermsChange,
                        colors = CheckboxDefaults.colors(checkedColor = ShivaColors.Brown)
                    )
                    Text(
                        buildAnnotatedString {
                            append("Acepto los ")
                            // terminos aun sin definir, por eso es solo texto subrayado
                            withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) { append("términos y condiciones") }
                            append(" y la política de privacidad.")
                        },
                        fontSize = 12.sp, color = ShivaColors.BrownText, lineHeight = 17.sp,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                // boton de registro: se activa solo si aceptan los terminos
                val enabled = uiState.submitEnabled
                Row(
                    Modifier.fillMaxWidth().height(56.dp)
                        .shadow(if (enabled) 8.dp else 0.dp, RoundedCornerShape(18.dp))
                        .clip(RoundedCornerShape(18.dp))
                        .background(ShivaColors.Brown)
                        .alpha(if (enabled) 1f else 0.45f)
                        .clickable(enabled = enabled, onClick = viewModel::onSubmit)
                        .padding(start = 20.dp, end = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Crear mi cuenta", color = Color.White, fontWeight = FontWeight.Bold,
                        fontSize = 15.sp, modifier = Modifier.weight(1f))
                    Box(Modifier.size(36.dp).clip(CircleShape).background(ShivaColors.Yellow),
                        contentAlignment = Alignment.Center) {
                        Icon(painterResource(R.drawable.ic_arrow_right), null, tint = ShivaColors.BrownDark)
                    }
                }

                Spacer(Modifier.height(16.dp))
                // texto de abajo, solo decorativo por ahora
                Text("¿Ya tienes una cuenta? Inicia sesión", fontSize = 12.sp, color = ShivaColors.BrownText,
                    modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

// etiqueta chiquita que va arriba de cada campo
@Composable
internal fun FieldLabel(text: String) {
    Text(text, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ShivaColors.BrownText,
        letterSpacing = 0.8.sp, modifier = Modifier.padding(bottom = 8.dp))
}

// selector tipo pildora para elegir el perfil
@Composable
private fun ProfileSelector(selected: ProfileType, onSelect: (ProfileType) -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Color(0xFFFFF1C9)).padding(6.dp)
    ) {
        // una opcion por cada perfil, la elegida se ve en blanco
        ProfileType.entries.forEach { type ->
            val isSelected = type == selected
            Row(
                Modifier.weight(1f).height(44.dp).clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) Color.White else Color.Transparent)
                    .clickable { onSelect(type) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painterResource(if (type == ProfileType.PRODUCTOR) R.drawable.ic_logo else R.drawable.ic_drone),
                    null, modifier = Modifier.size(14.dp),
                    tint = if (isSelected) ShivaColors.BrownDark else ShivaColors.BrownText
                )
                Spacer(Modifier.width(6.dp))
                Text(type.label, fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) ShivaColors.BrownDark else ShivaColors.BrownText)
            }
        }
    }
}

// campo de texto reutilizable: etiqueta, error y ojito para la clave
@Composable
internal fun ShivaTextField(
    label: String,
    value: String,
    placeholder: String,
    error: String?,
    onValueChange: (String) -> Unit,
    onBlur: () -> Unit,
    keyboardType: KeyboardType,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: () -> Unit = {}
) {
    // recuerda si el campo ya tuvo foco para no mostrar error antes de tiempo
    var hadFocus by remember { mutableStateOf(false) }
    FieldLabel(label)
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        isError = error != null,
        placeholder = { Text(placeholder, color = ShivaColors.Placeholder, fontSize = 14.sp) },
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        // el ojito solo aparece en el campo de clave
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = onTogglePassword) {
                    Icon(painterResource(if (passwordVisible) R.drawable.ic_eye_off else R.drawable.ic_eye),
                        "Mostrar u ocultar contraseña", tint = ShivaColors.BrownText)
                }
            }
        } else null,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White, unfocusedContainerColor = Color(0xFFFFFDF6),
            focusedBorderColor = ShivaColors.Brown, unfocusedBorderColor = ShivaColors.Border,
            errorBorderColor = ShivaColors.Error, errorContainerColor = Color(0xFFFFFDF6)
        ),
        // al perder el foco avisamos al viewmodel para que valide
        modifier = Modifier.fillMaxWidth().onFocusChanged {
            if (it.isFocused) hadFocus = true
            else if (hadFocus) onBlur()
        }
    )
    // mensaje de error justo debajo del campo
    if (error != null) {
        Text(error, color = ShivaColors.Error, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp, top = 4.dp))
    }
    Spacer(Modifier.height(16.dp))
}
