package com.example.filmlistan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.filmlistan.ui.theme.FilmlistanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { FilmlistanTheme { FilmListaSkarm() } }
    }
}

@Composable
fun FilmKort(film: Film, onToggleSedd: (Int) -> Unit, onTaBort: (Int) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(60.dp).background(Color(0xFF90CAF9)), contentAlignment = Alignment.Center) {
                if (film.bildRes != null) {
                    Image(
                        painter = painterResource(id = film.bildRes),
                        contentDescription = film.titel,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(text = film.titel.first().toString(), style = MaterialTheme.typography.headlineMedium)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = film.titel, style = MaterialTheme.typography.titleMedium)
                Text(text = "År: ${film.ar} | Genre: ${film.genre}")
                TextButton(onClick = { onTaBort(film.id) }) { Text("Ta bort", color = Color.Red) }
            }
            Checkbox(checked = film.sedd, onCheckedChange = { onToggleSedd(film.id) })
        }
    }
}

@Composable
fun FilmListaSkarm(viewModel: FilmlistanViewModel = viewModel()) {
    val uiState = viewModel.uiState
    val villSe = uiState.filmer.filter { !it.sedd }
    val sett = uiState.filmer.filter { it.sedd }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Min Topplista", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(value = uiState.nyTitel, onValueChange = { viewModel.uppdateraNyTitel(it) }, label = { Text("Ny titel") }, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel.laggTillFilm() }, enabled = uiState.nyTitel.trim().isNotEmpty()) { Text("Lägg till") }
        }
        LazyColumn {
            item { Text("Vill se (${villSe.size})", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(8.dp)) }
            items(villSe, key = { it.id }) { FilmKort(it, { viewModel.toggleSedd(it) }, { viewModel.taBortFilm(it) }) }
            item { Spacer(Modifier.height(16.dp)); Text("Sett (${sett.size})", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(8.dp)) }
            items(sett, key = { it.id }) { FilmKort(it, { viewModel.toggleSedd(it) }, { viewModel.taBortFilm(it) }) }
        }
    }
}