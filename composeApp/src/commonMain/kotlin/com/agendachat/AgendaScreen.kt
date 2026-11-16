package com.agendachat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private class Event(val start: String, val end: String, val title: String, val place: String, val tag: String)

private val events: Map<Int, List<Event>> = mapOf(
    9 to listOf(Event("09:00", "10:00", "Planificación de equipo", "Sala virtual", "Trabajo")),
    11 to listOf(Event("11:00", "12:00", "Entrega de propuesta", "Oficina", "Trabajo")),
    12 to listOf(Event("12:00", "13:00", "Reunión de seguimiento", "Sala virtual", "Trabajo")),
    13 to listOf(Event("13:00", "14:00", "Cierre de la semana", "Oficina", "Trabajo")),
    15 to listOf(
        Event("10:00", "11:00", "Revisión de proyecto", "Sala virtual · Equipo de diseño", "Trabajo"),
        Event("13:00", "14:00", "Almuerzo con Laura", "Café Central", "Personal"),
        Event("17:00", "18:00", "Lectura y planificación", "Casa", "Personal"),
    ),
)

private val weekDays = listOf("lun", "mar", "mié", "jue", "vie", "sáb", "dom")

@Composable
private fun EventCard(e: Event, highlight: Boolean = false) {
    AppCard(
        Modifier.fillMaxWidth().padding(bottom = 8.dp),
        color = if (highlight) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant,
    ) {
        TagChip(e.tag)
        Spacer(Modifier.height(6.dp))
        Text(e.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text("${e.start} – ${e.end} · ${e.place}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun AgendaScreen(nav: Nav, wide: Boolean) {
    var mode by remember { mutableStateOf(0) } // 0 mes, 1 semana, 2 día
    Page(
        title = "Agenda",
        subtitle = "Tu tiempo, en orden.",
        wide = wide,
        action = { if (wide) MintButton("+ Nueva cita", { nav.go(Screen.NewAppointment) }) else MintButton("+", { nav.go(Screen.NewAppointment) }) },
    ) {
        Segmented(listOf("Mes", "Semana", "Día"), mode) { mode = it }
        Spacer(Modifier.height(16.dp))
        when (mode) {
            0 -> MonthView(wide)
            1 -> WeekView()
            else -> DayView(wide)
        }
    }
}

@Composable
private fun DateHeader(text: String) {
    Row(Modifier.fillMaxWidth().padding(bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.weight(1f))
        Text("‹", fontSize = 22.sp, modifier = Modifier.padding(horizontal = 12.dp))
        Text("Hoy", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
        Text("›", fontSize = 22.sp, modifier = Modifier.padding(horizontal = 12.dp))
    }
}

@Composable
private fun MonthView(wide: Boolean) {
    var selected by remember { mutableStateOf(15) }
    // Noviembre 2026 empieza en domingo: 6 días de octubre antes y 6 de diciembre después.
    val cells = (26..31).map { it to false } + (1..30).map { it to true } + (1..6).map { it to false }

    val calendar: @Composable ColumnScope.() -> Unit = {
        DateHeader("Noviembre 2026")
        Row(Modifier.fillMaxWidth()) {
            weekDays.forEach {
                Text(it, Modifier.weight(1f), fontSize = 11.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.height(6.dp))
        cells.chunked(7).forEach { week ->
            Row(Modifier.fillMaxWidth()) {
                week.forEach { (day, current) ->
                    val on = current && day == selected
                    Box(
                        Modifier.weight(1f).height(42.dp).padding(2.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (on) Mint else Color.Transparent)
                            .clickable(enabled = current) { selected = day },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "$day",
                            color = when {
                                on -> OnMint
                                current -> MaterialTheme.colorScheme.onSurface
                                else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                            },
                            fontWeight = if (on || (current && events.containsKey(day))) FontWeight.Bold else FontWeight.Normal,
                        )
                    }
                }
            }
        }
    }

    val dayList: @Composable ColumnScope.() -> Unit = {
        SectionTitle("Domingo, $selected de noviembre".replace("Domingo", dayName(selected)))
        val list = events[selected].orEmpty()
        if (list.isEmpty()) Text("Sin citas", color = MaterialTheme.colorScheme.onSurfaceVariant)
        list.forEach { EventCard(it) }
    }

    if (wide) {
        Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
            Column(Modifier.weight(1.6f), content = calendar)
            Column(Modifier.weight(1f), content = dayList)
        }
    } else {
        Column() {
            calendar()
            Spacer(Modifier.height(16.dp))
            dayList()
        }
    }
}

private fun dayName(day: Int): String {
    // 1 de noviembre de 2026 = domingo
    val names = listOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")
    return names[(day - 1) % 7]
}

@Composable
private fun WeekView() {
    DateHeader("9 – 15 de noviembre")
    (9..15).forEachIndexed { i, d ->
        val list = events[d].orEmpty()
        Row(Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
            Column(Modifier.width(56.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(weekDays[i], fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("$d", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    color = if (d == 15) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
            }
            Column(Modifier.weight(1f)) {
                if (list.isEmpty()) {
                    Text("Sin citas", Modifier.padding(top = 10.dp, bottom = 10.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                }
                list.forEach { EventCard(it, highlight = d == 15 && it.start == "10:00") }
            }
        }
    }
}

@Composable
private fun DayView(wide: Boolean) {
    val list = events[15].orEmpty()
    DateHeader("Domingo, 15")
    val timeline: @Composable ColumnScope.() -> Unit = {
        list.forEachIndexed { i, e ->
            Row(Modifier.fillMaxWidth()) {
                Text(e.start, Modifier.width(56.dp).padding(top = 14.dp), fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Column(Modifier.weight(1f)) { EventCard(e, highlight = i == 0) }
            }
        }
    }
    val summary: @Composable ColumnScope.() -> Unit = {
        AppCard(Modifier.fillMaxWidth()) {
            Text("Tu día, en orden.", fontWeight = FontWeight.SemiBold)
            Text("0${list.size}", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text("citas", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
        }
    }
    if (wide) {
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Column(Modifier.weight(2f), content = timeline)
            Column(Modifier.weight(1f), content = summary)
        }
    } else {
        Column(){
        summary(); Spacer(Modifier.height(12.dp)); timeline()
        }
    }
}
