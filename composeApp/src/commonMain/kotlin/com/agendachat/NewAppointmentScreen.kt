package com.agendachat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NewAppointmentScreen(nav: Nav, wide: Boolean) {
    var title by remember { mutableStateOf("Planificación semanal") }
    var date by remember { mutableStateOf("16 de noviembre de 2026") }
    var from by remember { mutableStateOf("09:00") }
    var to by remember { mutableStateOf("10:00") }
    var place by remember { mutableStateOf("Sala virtual · meet.google.com") }
    var reminder by remember { mutableStateOf("15 minutos antes") }
    var notes by remember { mutableStateOf("") }

    val form: @Composable ColumnScope.() -> Unit = {
        AppField("Título", title) { title = it }
        AppField("Fecha", date) { date = it }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AppField("Desde", from, Modifier.weight(1f)) { from = it }
            AppField("Hasta", to, Modifier.weight(1f)) { to = it }
        }
        AppField("Lugar o enlace", place) { place = it }
        AppField("Recordatorio", reminder) { reminder = it }
        AppField("Notas", notes, minLines = 3) { notes = it }
        Spacer(Modifier.height(8.dp))
        MintButton("Guardar cita", { nav.back() }, Modifier.fillMaxWidth())
        TextButton(onClick = { nav.back() }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Cancelar", color = MaterialTheme.colorScheme.error)
        }
    }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(if (wide) 32.dp else 20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (!wide) IconButton(onClick = { nav.back() }) { Icon(Icons.Default.Close, "Cerrar") }
            Text("Nueva cita", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(20.dp))
        if (wide) {
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                Column(Modifier.weight(1.4f)) { SectionTitle("Detalles de la cita"); form() }
                Column(Modifier.weight(1f)) {
                    AppCard(Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.secondaryContainer) {
                        TagChip("Vista previa")
                        Spacer(Modifier.height(8.dp))
                        Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("$from – $to", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(12.dp))
                        Text("Lugar", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(place, fontSize = 13.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("Recordatorio", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(reminder, fontSize = 13.sp)
                    }
                }
            }
        } else {
            form()
        }
    }
}
