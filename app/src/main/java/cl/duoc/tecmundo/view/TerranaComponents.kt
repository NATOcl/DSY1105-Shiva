package cl.duoc.tecmundo.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.tecmundo.R
import cl.duoc.tecmundo.ui.theme.TerranaGreenDark
import cl.duoc.tecmundo.ui.theme.TerranaHintGray
import cl.duoc.tecmundo.ui.theme.TerranaInputBg
import cl.duoc.tecmundo.ui.theme.TerranaInputBorder
import cl.duoc.tecmundo.ui.theme.TerranaLabelGray
import cl.duoc.tecmundo.ui.theme.TerranaLime

// El ojo dentro del cuadrito lima
@Composable
fun TerranaLogoBadge(modifier: Modifier = Modifier, size: Dp = 44.dp) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(14.dp))
            .background(TerranaLime),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().padding(9.dp)
        )
    }
}

// El circulito lima con la flecha que va dentro de los botones
@Composable
fun ArrowCircle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(TerranaLime),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}

// Campo de texto con su título arriba, lo usamos en todo el formulario
@Composable
fun TerranaTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.Top) {
        // Título pequeño en mayúsculas
        Text(
            text = label,
            color = TerranaLabelGray,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(Modifier.height(8.dp))

        // El campo en sí, con bordes redondeados
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            placeholder = { Text(placeholder, color = TerranaHintGray, fontSize = 14.sp) },
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            // Colores para que combine con el diseño
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = TerranaInputBg,
                unfocusedContainerColor = TerranaInputBg,
                focusedBorderColor = TerranaGreenDark,
                unfocusedBorderColor = TerranaInputBorder,
                focusedTextColor = TerranaGreenDark,
                unfocusedTextColor = TerranaGreenDark,
                cursorColor = TerranaGreenDark
            )
        )
    }
}