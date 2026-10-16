package cl.duoc.tecmundo.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.tecmundo.R
import cl.duoc.tecmundo.model.RegisterFormState
import cl.duoc.tecmundo.ui.theme.TecMundoTheme
import cl.duoc.tecmundo.ui.theme.TerranaCream
import cl.duoc.tecmundo.ui.theme.TerranaGreenDark
import cl.duoc.tecmundo.ui.theme.TerranaGreenDisabled
import cl.duoc.tecmundo.ui.theme.TerranaInputBorder
import cl.duoc.tecmundo.ui.theme.TerranaLabelGray
import cl.duoc.tecmundo.ui.theme.TerranaLime
import cl.duoc.tecmundo.ui.theme.TerranaSubtitleBlue
import cl.duoc.tecmundo.view.components.ArrowCircle
import cl.duoc.tecmundo.view.components.TerranaTextField

/**
 * VISTA: formulario de registro (paso 1 de 2).
 * Los campos no envían nada; solo el check de términos habilita el botón.
 */
@Composable
fun RegisterScreen(
    form: RegisterFormState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val termsPrefix = stringResource(R.string.terms_prefix)
    val termsLink = stringResource(R.string.terms_link)
    val termsSuffix = stringResource(R.string.terms_suffix)
    val accountPrefix = stringResource(R.string.register_have_account)
    val accountLink = stringResource(R.string.register_login)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TerranaCream)
            .background(
                Brush.radialGradient(
                    colors = listOf(TerranaLime.copy(alpha = 0.28f), Color.Transparent),
                    center = Offset(Float.POSITIVE_INFINITY, 0f),
                    radius = 900f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Barra superior
            Box(modifier = Modifier.fillMaxWidth().height(44.dp)) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .align(Alignment.CenterStart)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, TerranaInputBorder, CircleShape)
                        .clickable(onClick = onBackClick),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_back),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_logo),
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.terrana_name),
                        color = TerranaGreenDark,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Progreso
            Spacer(Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(Modifier.width(28.dp).height(6.dp).clip(RoundedCornerShape(4.dp)).background(TerranaGreenDark))
                Box(Modifier.width(10.dp).height(6.dp).clip(RoundedCornerShape(4.dp)).background(TerranaLime))
            }

            Spacer(Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.register_step),
                color = TerranaLabelGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.register_title),
                color = TerranaGreenDark,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.register_subtitle),
                color = TerranaSubtitleBlue,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            // Campos (sin acción)
            Spacer(Modifier.height(18.dp))
            TerranaTextField(
                label = stringResource(R.string.label_full_name),
                value = form.fullName,
                onValueChange = { form.fullName = it },
                placeholder = stringResource(R.string.hint_full_name),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
            )
            Spacer(Modifier.height(18.dp))
            TerranaTextField(
                label = stringResource(R.string.label_email),
                value = form.email,
                onValueChange = { form.email = it },
                placeholder = stringResource(R.string.hint_email),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(Modifier.height(18.dp))
            TerranaTextField(
                label = stringResource(R.string.label_farm),
                value = form.farmName,
                onValueChange = { form.farmName = it },
                placeholder = stringResource(R.string.hint_farm),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
            )
            Spacer(Modifier.height(18.dp))
            TerranaTextField(
                label = stringResource(R.string.label_password),
                value = form.password,
                onValueChange = { form.password = it },
                placeholder = stringResource(R.string.hint_password),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (form.passwordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_eye),
                        contentDescription = null,
                        tint = TerranaLabelGray,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { form.passwordVisible = !form.passwordVisible }
                            .padding(8.dp)
                            .size(24.dp)
                    )
                }
            )

            // Términos y condiciones (texto por definir)
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = form.termsAccepted,
                    onCheckedChange = { form.termsAccepted = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = TerranaGreenDark,
                        uncheckedColor = TerranaSubtitleBlue
                    )
                )
                Text(
                    text = buildAnnotatedString {
                        append(termsPrefix)
                        withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append(termsLink)
                        }
                        append(termsSuffix)
                    },
                    color = TerranaSubtitleBlue,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }

            // Botón de registro: deshabilitado hasta marcar el check; sin acción
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { /* sin acción por ahora */ },
                enabled = form.canRegister,
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TerranaGreenDark,
                    contentColor = Color.White,
                    disabledContainerColor = TerranaGreenDisabled,
                    disabledContentColor = Color.White
                ),
                contentPadding = PaddingValues(start = 20.dp, end = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.register_button),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    ArrowCircle()
                }
            }

            Spacer(Modifier.height(20.dp))
            Text(
                text = buildAnnotatedString {
                    append(accountPrefix)
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(accountLink) }
                },
                modifier = Modifier.fillMaxWidth(),
                color = TerranaSubtitleBlue,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun RegisterScreenPreview() {
    TecMundoTheme { RegisterScreen(form = RegisterFormState(), onBackClick = {}) }
}
