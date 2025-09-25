package pt.isel.pdm.about

import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.chelasmulti_playerpokerdice.R
import pt.isel.pdm.actions.ActionsIntent
import pt.isel.pdm.ui.author.Author
import pt.isel.pdm.ui.background.DefaultBackGround
import pt.isel.pdm.ui.topBar.TopBarConfig


@Composable
fun AboutScreen(
    onDetails: (action: ActionsIntent) -> Unit,
    onSendEmail: (action: ActionsIntent) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val groupMembers = listOf("André Nunes - 51766", "Guilherme Coutinho - 50467")
    val emails = listOf("A51766@alunos.isel.pt", "A50467@alunos.isel.pt")

    DefaultBackGround(
        {
            Text(
                text = stringResource(R.string.description_game),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        {
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
        },
        {
            Text("Members of group:", style = MaterialTheme.typography.titleMedium)
            groupMembers.forEach { member ->
                Text(text = member)
            }
        },
        {
            Button(onClick = {
                onSendEmail(ActionsIntent.Email(emails, context))
            }) {
                Text("Contactar todos os membros")
            }
        },
        {
            Author(onSendEmailRequested = {})
        },
        topBarConfig = TopBarConfig.WithBack(
            title = "About the game",
            onBack = onBack
        ),
        modifier = Modifier
    )
}


@Preview(showBackground = true)
@Composable
fun Teste(){
    AboutScreen({}, {},{})
}