package com.agendachat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private class NavItem(val label: String, val icon: ImageVector, val target: Screen)

private val items = listOf(
    NavItem("Inicio", Icons.Default.Home, Screen.Home),
    NavItem("Agenda", Icons.Default.DateRange, Screen.Agenda),
    NavItem("Chats", Icons.Default.Email, Screen.Chats),
    NavItem("Ajustes", Icons.Default.Settings, Screen.Profile),
)

@Composable
fun App() {
    val systemDark = isSystemInDarkTheme()
    val nav = remember { Nav(systemDark) }
    AgendaTheme(nav.dark) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            BoxWithConstraints(Modifier.fillMaxSize().safeDrawingPadding()) {
                val wide = maxWidth >= 840.dp
                if (nav.current == Screen.Login) LoginScreen(nav, wide) else Shell(nav, wide)
            }
        }
    }
}

@Composable
private fun Shell(nav: Nav, wide: Boolean) {
    val s = nav.current
    if (wide) {
        Row(Modifier.fillMaxSize()) {
            Sidebar(nav)
            VerticalDivider(color = MaterialTheme.colorScheme.outline)
            Box(Modifier.weight(1f)) { ScreenContent(nav, wide) }
        }
    } else {
        val fullscreen = s is Screen.NewAppointment || s is Screen.Chat
        Column(Modifier.fillMaxSize()) {
            Box(Modifier.weight(1f)) { ScreenContent(nav, wide) }
            if (!fullscreen) BottomBar(nav)
        }
    }
}

@Composable
private fun ScreenContent(nav: Nav, wide: Boolean) {
    when (val s = nav.current) {
        Screen.Login -> Unit
        Screen.Home -> HomeScreen(nav, wide)
        Screen.Chats -> ChatsScreen(nav, wide, null)
        is Screen.Chat -> ChatsScreen(nav, wide, s.name)
        Screen.Agenda -> AgendaScreen(nav, wide)
        Screen.NewAppointment -> NewAppointmentScreen(nav, wide)
        Screen.Profile -> ProfileScreen(nav, wide)
        Screen.Notifications -> NotificationsScreen(nav, wide)
    }
}

@Composable
private fun Sidebar(nav: Nav) {
    val selected = nav.current.tabIndex()
    Column(Modifier.width(220.dp).fillMaxHeight().padding(16.dp)) {
        Text("AgendaChat", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(24.dp))
        Text("MENÚ", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        items.forEachIndexed { i, item ->
            val on = i == selected
            Row(
                Modifier.fillMaxWidth().padding(vertical = 2.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (on) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent)
                    .clickable { nav.tab(item.target) }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(item.icon, null, Modifier.size(18.dp),
                    tint = if (on) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.width(10.dp))
                Text(item.label, fontWeight = if (on) FontWeight.SemiBold else FontWeight.Normal)
            }
        }
        Spacer(Modifier.weight(1f))
        Text("Ana Martín", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        Text("ana.martin@correo.com", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun BottomBar(nav: Nav) {
    val selected = nav.current.tabIndex()
    NavigationBar(containerColor = MaterialTheme.colorScheme.background) {
        items.forEachIndexed { i, item ->
            NavigationBarItem(
                selected = i == selected,
                onClick = { nav.tab(item.target) },
                icon = { Icon(item.icon, item.label) },
                label = { Text(item.label, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                ),
            )
        }
    }
}
