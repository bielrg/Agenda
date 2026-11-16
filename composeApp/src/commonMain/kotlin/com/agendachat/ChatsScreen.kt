package com.agendachat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private class ChatPreview(val name: String, val last: String, val time: String, val unread: Int)

private val chats = listOf(
    ChatPreview("Equipo de diseño", "Lucía: ¿Revisamos el proyecto a las 10?", "09:41", 2),
    ChatPreview("Planes de la semana", "Pablo: Perfecto, confirmado.", "Ayer", 0),
    ChatPreview("Club de lectura", "Marta: Esta noche nos vemos a las 8", "Vie", 0),
    ChatPreview("Proyecto personal", "Tú: Te comparto la agenda.", "Jue", 0),
)

private class Msg(val author: String, val text: String, val time: String, val mine: Boolean, val card: Boolean = false)

private fun initialMessages() = listOf(
    Msg("Lucía", "Buenos días! ¿Revisamos el proyecto a las 10?", "09:41", false),
    Msg("Tú", "¡Sí! Ya lo tengo en la agenda. Ahora comparto la cita.", "09:42", true),
    Msg("Tú", "Revisión de proyecto\n10:00 – 11:00 · Sala virtual", "09:42", true, card = true),
    Msg("Marcos", "Perfecto, nos vemos en la sala.", "09:44", false),
    Msg("Lucía", "¡Nos vemos a las 10!", "09:45", false),
)

@Composable
fun ChatsScreen(nav: Nav, wide: Boolean, selected: String?) {
    if (wide) {
        Row(Modifier.fillMaxSize()) {
            Box(Modifier.width(340.dp).fillMaxHeight()) { ChatList(nav, selected, wide) }
            VerticalDivider(color = MaterialTheme.colorScheme.outline)
            Box(Modifier.weight(1f).fillMaxHeight()) {
                if (selected == null) EmptyChat(nav) else Conversation(nav, selected, wide)
            }
        }
    } else {
        if (selected == null) ChatList(nav, null, wide) else Conversation(nav, selected, wide)
    }
}

@Composable
private fun EmptyChat(nav: Nav) {
    Column(Modifier.fillMaxSize().padding(32.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
        Text("Tus conversaciones,\nsiempre a mano.", fontSize = 28.sp, fontWeight = FontWeight.Bold, lineHeight = 34.sp)
        Spacer(Modifier.height(8.dp))
        Text("Organiza y chatea con tu equipo.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))
        MintButton("+ Nueva conversación", { nav.openChat(chats.first().name) })
    }
}

@Composable
private fun ChatList(nav: Nav, selected: String?, wide: Boolean) {
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf(0) }
    val shown = chats.filter { (filter == 0 || it.unread > 0) && it.name.contains(query, ignoreCase = true) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(if (wide) 20.dp else 20.dp)) {
        Text("Chats", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Consulta y organiza en equipo.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))
        AppField("", query, Modifier.padding(bottom = 4.dp)) { query = it }
        Segmented(listOf("Todos", "Sin leer"), filter) { filter = it }
        Spacer(Modifier.height(12.dp))
        shown.forEach { c ->
            val on = c.name == selected
            AppCard(
                Modifier.fillMaxWidth().padding(bottom = 8.dp),
                color = if (on) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                onClick = { nav.openChat(c.name) },
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(c.name, fontWeight = FontWeight.SemiBold)
                        Text(c.last, fontSize = 12.sp, maxLines = 1, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(c.time, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (c.unread > 0) { Spacer(Modifier.height(4.dp)); Badge(c.unread) }
                    }
                }
            }
        }
    }
}

@Composable
private fun Conversation(nav: Nav, name: String, wide: Boolean) {
    val messages = remember(name) { mutableStateListOf(*initialMessages().toTypedArray()) }
    var draft by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (!wide) IconButton(onClick = { nav.back() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") }
            Column(Modifier.padding(start = if (wide) 8.dp else 0.dp)) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("4 miembros · En línea", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Hoy · 15 nov", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally))
            messages.forEach { m ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = if (m.mine) Arrangement.End else Arrangement.Start) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (m.mine) Mint else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (m.mine) OnMint else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.widthIn(max = 340.dp),
                    ) {
                        Column(Modifier.padding(10.dp)) {
                            if (!m.mine) Text(m.author, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                            if (m.card) Text("CITA", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text(m.text, fontWeight = if (m.card) FontWeight.SemiBold else FontWeight.Normal)
                            Text(m.time, fontSize = 10.sp, modifier = Modifier.align(Alignment.End))
                        }
                    }
                }
            }
        }
        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = draft, onValueChange = { draft = it }, modifier = Modifier.weight(1f),
                singleLine = true, shape = RoundedCornerShape(8.dp),
                placeholder = { Text("Escribe un mensaje…") },
            )
            Spacer(Modifier.width(8.dp))
            MintButton("Enviar", {
                if (draft.isNotBlank()) { messages.add(Msg("Tú", draft, "ahora", true)); draft = "" }
            })
        }
    }
}
