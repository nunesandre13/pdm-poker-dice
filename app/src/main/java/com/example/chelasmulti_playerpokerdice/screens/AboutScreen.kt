package com.example.chelasmulti_playerpokerdice.screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {
    val groupMembers = listOf(
        "André Nunes - 51766",
        "GUI CUTINHO - 234567")
    val emails = listOf("A51766@blablaisel.com")

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Bem-vindo ao Poker Dice!\n\nO Poker Dice é um jogo de dados onde você tenta formar as melhores combinações de poker com cinco dados. Para mais detalhes, acesse:",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Descrição detalhada",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    fetchAboutOnBrowser()
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Membros do grupo:", style = MaterialTheme.typography.titleMedium)
            groupMembers.forEach { member ->
                Text(text = member)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
               fetchMembers()
            }) {
                Text("Contactar todos os membros")
            }
        }
    }
}

fun fetchMembers() {
    TODO()
}

fun fetchAboutOnBrowser() {

}

@Preview(showBackground = true)
@Composable
fun Teste(){
    AboutScreen()
}