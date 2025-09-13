package pt.isel.pdm.screens

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
    val groupMembers = listOf("André Nunes - 51766", "Guilherme Coutinho - 50467")
    val emails = listOf("A51766@alunos.isel.pt","A50467@alunos.isel.pt")

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Poker Dice is a dice game inspired by poker where each player rolls five dice and may re-roll up to two times to form the best hand. After all players finish, their combinations are compared and the highest hand wins. Hands rank from strongest to weakest as: Five of a Kind, Four of a Kind, Full House, Straight, Three of a Kind, Two Pair, One Pair, and Bust.",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Detailed description",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    fetchAboutOnBrowser()
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Members of group:", style = MaterialTheme.typography.titleMedium)
            groupMembers.forEach { member ->
                Text(text = member)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
               fetchMembers()
            }) {
                Text("Contact all members")
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