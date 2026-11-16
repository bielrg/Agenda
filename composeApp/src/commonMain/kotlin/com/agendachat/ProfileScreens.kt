package com.agendachat

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
private fun PrefRow(title: String, value: String, onClick: (() -> Unit)? = null, trailing: (@Composable () -> Unit)? = null) {
    AppCard(Modifier.fillMaxWidth().padding(bottom = 8.dp), onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(value, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (trailing != null) trailing()
            else Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun ProfileScreen(nav: Nav, wide: Boolean) {
    var name by remember { mutableStateOf("Ana Martín") }
    var email by remember { mutableStateOf("ana.martin@correo.com") }

    Page("Mi perfil", "Gestiona tu cuenta y preferencias.", wide) {
        val personal: @Composable ColumnScope.() -> Unit = {
            SectionTitle("Información personal")
            AppField("Nombre completo", name) { name = it }
            AppField("Correo electrónico", email) { email = it }
        }
        val prefs: @Composable ColumnScope.() -> Unit = {
            SectionTitle("Preferencias")
            PrefRow("Notificaciones", "Personalizar", onClick = { nav.go(Screen.Notifications) })
            PrefRow("Apariencia", if (nav.dark) "Oscuro" else "Claro", trailing = {
                Switch(checked = nav.dark, onCheckedChange = { nav.dark = it })
            })
            PrefRow("Zona horaria", "Madrid (GMT+1)")
        }
        if (wide) {
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                Column(Modifier.weight(1f), content = personal)
                Column(Modifier.weight(1f), content = prefs)
            }
        } else {
            personal(); Spacer(Modifier.height(12.dp)); prefs()
        }
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MintButton("Guardar cambios", {})
            TextButton(onClick = { nav.tab(Screen.Login) }) {
                Text("Cerrar sesión", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun NotificationsScreen(nav: Nav, wide: Boolean) {
    var reminders by remember { mutableStateOf(true) }
    var chat by remember { mutableStateOf(true) }
    var sound by remember { mutableStateOf(false) }

    @Composable
    fun ToggleRow(title: String, desc: String, checked: Boolean, onChange: (Boolean) -> Unit) =
        PrefRow(title, desc, trailing = { Switch(checked = checked, onCheckedChange = onChange) })

    Page("Notificaciones", "Elige cómo quieres estar al día.", wide,
        action = { TextButton(onClick = { nav.back() }) { Text("Volver") } }) {
        SectionTitle("Recordatorios")
        ToggleRow("Recordatorios de citas", "No te pierdas tu próxima cita.", reminders) { reminders = it }
        PrefRow("Avisarme con antelación", "15 minutos antes")
        Spacer(Modifier.height(12.dp))
        SectionTitle("Mensajes")
        ToggleRow("Mensajes de chat", "Nuevos mensajes en tus chats.", chat) { chat = it }
        ToggleRow("Sonido y vibración", "Alertas silenciosas.", sound) { sound = it }
        Spacer(Modifier.height(8.dp))
        Text("Los cambios se guardan automáticamente.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
