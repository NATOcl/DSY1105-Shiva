package cl.duoc.tecmundo.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.tecmundo.model.SampleData



private enum class AdminTab(val label: String) {
    USUARIOS("Productor"), PRESTADORES("Aplicador"), SOLICITUDES("Solicitudes")
}

@Composable
fun AdminHomeScreen(
    onLogOut: () -> Unit = {}   // vuelve a la bienvenida desde el menu lateral
) {
    var tab by rememberSaveable { mutableStateOf(AdminTab.USUARIOS) }
// el avatar abre el menu lateral, igual que en los otros perfiles
    ProfileDrawerHost(userName = "Administrador", role = "Administrador", onLogOut = onLogOut) { openDrawer ->
        HomeBackground {
            HomeHeader(userName = "Administrador", greeting = "BIENVENIDO", onAvatarClick = openDrawer)
            Spacer(Modifier.height(20.dp))
            Text("PANEL DE ADMINISTRACIÓN", fontSize = 11.sp, fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp, color = HomeColors.Eyebrow)
            Spacer(Modifier.height(6.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Datos de prueba", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold,
                    color = HomeColors.Title, letterSpacing = (-0.5).sp, modifier = Modifier.weight(1f))
                CountPill("Simulados")
            }
            Spacer(Modifier.height(8.dp))
            Text("Información ficticia para probar el MVP. Solo lectura.",
                fontSize = 14.sp, color = HomeColors.Subtitle, lineHeight = 20.sp)

            Spacer(Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(50)).background(Color.White).padding(4.dp)
            ) {
                AdminTab.entries.forEach { t ->
                    val on = t == tab
                    Box(
                        Modifier.weight(1f).clip(RoundedCornerShape(50))
                            .background(if (on) ShivaColors.Yellow else Color.Transparent)
                            .clickable { tab = t }.padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(t.label, fontSize = 13.sp,
                            fontWeight = if (on) FontWeight.Bold else FontWeight.Medium,
                            color = ShivaColors.BrownDark)
                    }
                }

            }
            Spacer(Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                when (tab) {
                    AdminTab.USUARIOS -> items(SampleData.users, key = { it.id }) {
                        InfoCard(it.id, it.role, listOf(it.zone, it.detail))
                    }
                    AdminTab.PRESTADORES -> items(SampleData.providers, key = { it.id }) {
                        InfoCard(
                            it.id,
                            if (it.available) "Disponible" else "No disponible",
                            listOf(
                                "Cobertura: ${it.zones}",
                                "Cultivos: ${it.crops}",
                                "Servicios: ${it.services}",
                                "${it.capacity} · ${it.experience} de experiencia"
                            )
                        )
                    }
                    AdminTab.SOLICITUDES -> items(SampleData.requests, key = { it.id }) {
                        InfoCard(
                            "${it.id} · ${it.ownerId}",
                            it.status,
                            listOf(it.summary, "${it.zone} · ${it.schedule}")
                        )
                    }
                }
            }
        }
    }

}
@Composable
private fun InfoCard(title: String, badge: String, lines: List<String>) {
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Color.White).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = HomeColors.Title,
                modifier = Modifier.weight(1f))
            Text(badge, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = HomeColors.Eyebrow)
        }
        lines.forEach {
            Spacer(Modifier.height(4.dp))
            Text(it, fontSize = 12.sp, color = HomeColors.Subtitle)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdminHomePreview() {
    TecMundoTheme { AdminHomeScreen() }
}
