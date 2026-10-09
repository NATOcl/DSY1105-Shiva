package cl.duoc.tecmundo

import android.graphics.Color.red
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cl.duoc.tecmundo.ui.theme.TecMundoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TecMundoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var nombre by remember { mutableStateOf("") }
    var modal by remember { mutableStateOf(false) }
    var terminos by remember { mutableStateOf(false) }
    var juegoSeleccionado by remember { mutableStateOf(0) }

    val juegos = listOf("God of War", "FIFA", "Kenshi", "Minecraft")

    fun verificarName(nombre: String): Boolean {
        return nombre.trim().length >= 3 && nombre.all { it.isLetter() || it.isWhitespace() }
    }

    fun abrirModal() { modal = true }
    fun cerrarModal() { modal = false }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        juegos.forEachIndexed { index, opcion ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = juegoSeleccionado == index,
                    onClick = { juegoSeleccionado = index }
                )
                Text(opcion)
            }
        }

        Checkbox(
            checked = terminos,
            onCheckedChange = { terminos = it }
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Ingresa nombre") },
            isError = !verificarName(nombre),
            supportingText = {
                if(verificarName(nombre)){
                    Text("Debes ingresar un nombre", color = Red)
                }
            }
        )

        Button(onClick = { abrirModal() }) {
            Text(text = "Enviar")
        }
    }

    if (modal) {
        AlertDialog(
            onDismissRequest = { cerrarModal() },
            title = { Text("Confirmación") },
            text = { Text("Formulario enviado correctamente") },
            confirmButton = {
                Button(onClick = { cerrarModal() }) {
                    Text("OK")
                }
            }
        )
    }
}