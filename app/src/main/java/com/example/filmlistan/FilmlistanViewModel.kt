package com.example.filmlistan

import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class Film(
    val id: Int,
    val titel: String,
    val ar: Int,
    val genre: String,
    val sedd: Boolean = false,
    @DrawableRes val bildRes: Int? = null
)

data class FilmlistanUiState(
    val filmer: List<Film> = emptyList(),
    val nyTitel: String = ""
)

val startfilmer = listOf(
    Film(id = 1, titel = "Norrskensväktaren", ar = 2024, genre = "Äventyr", bildRes = R.drawable.film1),
    Film(id = 2, titel = "Den sista bussen", ar = 2021, genre = "Drama", bildRes = null),
    Film(id = 3, titel = "Kodnamn Granit", ar = 2023, genre = "Thriller", bildRes = null),
)

class FilmlistanViewModel : ViewModel() {
    var uiState by mutableStateOf(FilmlistanUiState(filmer = startfilmer))
        private set
    private var nextId = 4

    fun uppdateraNyTitel(titel: String) { uiState = uiState.copy(nyTitel = titel) }

    fun laggTillFilm() {
        val titel = uiState.nyTitel.trim()
        if (titel.isEmpty()) return
        val nyFilm = Film(id = nextId, titel = titel, ar = 2025, genre = "Okänd")
        nextId++
        uiState = uiState.copy(filmer = uiState.filmer + nyFilm, nyTitel = "")
    }

    fun toggleSedd(id: Int) {
        uiState = uiState.copy(filmer = uiState.filmer.map { if (it.id == id) it.copy(sedd = !it.sedd) else it })
    }

    fun taBortFilm(id: Int) {
        uiState = uiState.copy(filmer = uiState.filmer.filter { it.id != id })
    }
}