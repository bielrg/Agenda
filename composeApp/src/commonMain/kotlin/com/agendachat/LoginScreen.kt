package com.agendachat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(nav: Nav, wide: Boolean) {
    var email by remember { mutableStateOf("ana.martin@correo.com") }
    var pass by remember { mutableStateOf("password") }
    val muted = MaterialTheme.colorScheme.onSurfaceVariant

    val form: @Composable ColumnScope.() -> Unit = {
        Text("Iniciar sesión", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Tu agenda y tus conversaciones, en un mismo lugar.", color = muted, fontSize = 13.sp)
        Spacer(Modifier.height(20.dp))
        AppField("Correo electrónico", email) { email = it }
        AppField("Contraseña", pass, password = true) { pass = it }
        Text("¿Olvidaste tu contraseña?", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
        Spacer(Modifier.height(16.dp))
        MintButton("Iniciar sesión", { nav.tab(Screen.Home) }, Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
        Row(Modifier.align(Alignment.CenterHorizontally)) {
            Text("¿Aún no tienes cuenta? ", color = muted, fontSize = 13.sp)
            Text("Regístrate", color = MaterialTheme.colorScheme.primary, fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { nav.tab(Screen.Home) })
        }
    }

    if (wide) {
        Row(Modifier.fillMaxSize()) {
            Box(Modifier.weight(1f).fillMaxHeight().background(MaterialTheme.colorScheme.secondaryContainer).padding(48.dp)) {
                Text("AgendaChat", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold,
                    fontSize = 20.sp, modifier = Modifier.align(Alignment.TopStart))
                Column(Modifier.align(Alignment.CenterStart)) {
                    Text("Tu día,\nconectado.", fontSize = 44.sp, fontWeight = FontWeight.Bold, lineHeight = 50.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer)
                    Spacer(Modifier.height(12.dp))
                    Text("Tu agenda y tus conversaciones, en un mismo lugar.", color = muted)
                }
            }
            Box(Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                Column(Modifier.widthIn(max = 400.dp).padding(24.dp), content = form)
            }
        }
    } else {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp)) {
            Spacer(Modifier.height(32.dp))
            Text("AgendaChat", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Spacer(Modifier.height(16.dp))
            Text("Tu día, conectado.", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(32.dp))
            form()
        }
    }
}
