package com.agendachat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed interface Screen {
    data object Login : Screen
    data object Home : Screen
    data object Chats : Screen
    data class Chat(val name: String) : Screen
    data object Agenda : Screen
    data object NewAppointment : Screen
    data object Profile : Screen
    data object Notifications : Screen
}

class Nav(startDark: Boolean) {
    private val stack = mutableStateListOf<Screen>(Screen.Login)
    val current: Screen get() = stack.last()
    var dark by mutableStateOf(startDark)

    fun go(s: Screen) { stack.add(s) }
    fun tab(s: Screen) { stack.clear(); stack.add(s) }
    fun openChat(name: String) {
        if (current is Screen.Chat) stack[stack.lastIndex] = Screen.Chat(name) else stack.add(Screen.Chat(name))
    }
    fun back() {
        if (stack.size > 1) stack.removeAt(stack.lastIndex) else tab(Screen.Home)
    }
}

/** 0 = Inicio, 1 = Agenda, 2 = Chats, 3 = Ajustes */
fun Screen.tabIndex(): Int = when (this) {
    Screen.Home, Screen.Login -> 0
    Screen.Agenda, Screen.NewAppointment -> 1
    Screen.Chats, is Screen.Chat -> 2
    Screen.Profile, Screen.Notifications -> 3
}
