package com.agendachat

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
private fun StatCard(number: String, label: String, modifier: Modifier = Modifier) {
    AppCard(modifier) {
        Text(number, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
    }
}

@Composable
fun HomeScreen(nav: Nav, wide: Boolean) {
    Page(
        title = "Hola, Ana",
        subtitle = "Domingo, 15 de noviembre",
        wide = wide,
        action = { if (wide) MintButton("+ Nueva cita", { nav.go(Screen.NewAppointment) }) },
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("03", "Citas hoy", Modifier.weight(1f))
            StatCard("02", "Chats sin leer", Modifier.weight(1f))
        }
        Spacer(Modifier.height(24.dp))

        val next: @Composable ColumnScope.() -> Unit = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SectionTitle("Tu próxima cita", Modifier.weight(1f))
                Text("Ver agenda", color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
            }
            AppCard(Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.secondaryContainer) {
                TagChip("Próxima cita")
                Spacer(Modifier.height(8.dp))
                Text("Revisión de proyecto", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("10:00 – 11:00 · Sala virtual", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(12.dp))
                MintButton("Ver en agenda", { nav.tab(Screen.Agenda) })
            }
            Spacer(Modifier.height(12.dp))
            MintButton("+ Nueva cita", { nav.go(Screen.NewAppointment) })
        }

        val convs: @Composable ColumnScope.() -> Unit = {
            SectionTitle("Conversaciones")
            AppCard(Modifier.fillMaxWidth(), onClick = { nav.openChat("Equipo de diseño") }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Equipo de diseño", fontWeight = FontWeight.SemiBold)
                        Text("Lucía: ¿Revisamos el proyecto a las 10?", fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Badge(2)
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("Tus conversaciones, siempre a mano.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        if (wide) {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Column(Modifier.weight(1.4f), content = next)
                Column(Modifier.weight(1f), content = convs)
            }
        } else {
            next()
            Spacer(Modifier.height(24.dp))
            convs()
        }
    }
}
