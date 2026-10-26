package cl.duoc.tecmundo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import cl.duoc.tecmundo.model.LoginResult
import cl.duoc.tecmundo.model.ProfileType
import cl.duoc.tecmundo.ui.theme.ShivaColors
import cl.duoc.tecmundo.viewmodel.Field
import cl.duoc.tecmundo.viewmodel.LoginViewModel

// pantalla de login: correo y clave, con el perfil que se eligio en la bienvenida
@Composable
fun LoginScreen(
    profile: ProfileType,
    onBack: () -> Unit,
    onLoggedIn: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val profileName = if (profile == ProfileType.PRODUCTOR) "productor" else "aplicador"

    // al salir de la pantalla el formulario queda vacio
    DisposableEffect(Unit) { onDispose { viewModel.reset() } }

    // dialogo con el resultado: exito, datos incorrectos o sin ese perfil
    uiState.result?.let { result ->
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    when (result) {
                        is LoginResult.Success -> "Inicio de sesión exitoso"
                        is LoginResult.InvalidCredentials -> "No se pudo iniciar sesión"
                        is LoginResult.NoProfile -> "Perfil no encontrado"
                    }
                )
            },
            text = {
                Text(
                    when (result) {
                        is LoginResult.Success -> "Iniciaste sesión como $profileName."
                        is LoginResult.InvalidCredentials -> "El correo o la contraseña no son correctos."
                        is LoginResult.NoProfile -> "No tienes perfil de $profileName."
                    }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onResultHandled()
                    if (result is LoginResult.Success) onLoggedIn()
                }) { Text(if (result is LoginResult.Success) "Continuar" else "Aceptar") }
            }
        )
    }

    // mismo fondo crema del registro
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

            Column(
                Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp)
            ) {
                Spacer(Modifier.height(10.dp))
                // titulos de la pantalla
                Text("ENTRANDO COMO ${profileName.uppercase()}", fontSize = 12.sp, fontWeight = FontWeight.Bold,
                    color = ShivaColors.BrownText, letterSpacing = 1.sp)
                Spacer(Modifier.height(4.dp))
                Text("Inicia sesión", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold,
                    color = ShivaColors.BrownDark, letterSpacing = (-0.5).sp)
                Spacer(Modifier.height(8.dp))
                Text("Ingresa tu correo y contraseña para acceder a tu espacio.",
                    fontSize = 14.sp, color = Color(0xFF6B5B2E), lineHeight = 20.sp)

                Spacer(Modifier.height(24.dp))
                // mismos campos que el registro, cada uno con su validacion
                ShivaTextField("CORREO ELECTRÓNICO", uiState.email, "nombre@correo.com",
                    uiState.emailError, viewModel::onEmailChange, { viewModel.onFieldBlur(Field.EMAIL) },
                    KeyboardType.Email)
                ShivaTextField("CONTRASEÑA", uiState.password, "Tu contraseña",
                    uiState.passwordError, viewModel::onPasswordChange, { viewModel.onFieldBlur(Field.PASSWORD) },
                    KeyboardType.Password,
                    isPassword = true, passwordVisible = uiState.passwordVisible,
                    onTogglePassword = viewModel::togglePasswordVisible)

                Spacer(Modifier.height(8.dp))

                // boton de login: se desactiva mientras se revisan los datos
                val enabled = !uiState.loading
                Row(
                    Modifier.fillMaxWidth().height(56.dp)
                        .shadow(if (enabled) 8.dp else 0.dp, RoundedCornerShape(18.dp))
                        .clip(RoundedCornerShape(18.dp))
                        .background(ShivaColors.Brown)
                        .alpha(if (enabled) 1f else 0.45f)
                        .clickable(enabled = enabled) { viewModel.onSubmit(profile) }
                        .padding(start = 20.dp, end = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Iniciar sesión", color = Color.White, fontWeight = FontWeight.Bold,
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