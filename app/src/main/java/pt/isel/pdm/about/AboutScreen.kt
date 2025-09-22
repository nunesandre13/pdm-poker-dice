package pt.isel.pdm.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chelasmulti_playerpokerdice.R
import pt.isel.pdm.actions.ActionsIntent

import androidx.compose.material3.TopAppBarDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onDetails: (action: ActionsIntent) -> Unit,
    onSendEmail : (action: ActionsIntent) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val groupMembers = listOf("André Nunes - 51766", "Guilherme Coutinho - 50467")
    val emails = listOf("A51766@alunos.isel.pt","A50467@alunos.isel.pt")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("About the game") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Voltar ao Menu"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.description_game),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                color = Color.Blue,
                text = "Mais info sobre Poker Dice",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.clickable {
                    onDetails(
                        ActionsIntent.Browser("https://en.wikipedia.org/wiki/Poker_dice", context)
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Membros do grupo:", style = MaterialTheme.typography.titleMedium)
            groupMembers.forEach { member ->
                Text(text = member)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                onSendEmail(ActionsIntent.Email(emails, context))
            }) {
                Text("Contactar todos os membros")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Teste(){
    AboutScreen({}, {},{})
}