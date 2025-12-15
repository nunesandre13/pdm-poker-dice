package pt.isel.pdm.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = stringResource(R.string.description_game),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }}
        ,
        {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 2.dp)
                    .clickable {
                        onDetails(
                            ActionsIntent.Browser(
                                "https://en.wikipedia.org/wiki/Poker_dice",
                                context
                            )
                        )
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceDim),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "See more about Poker Dice",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "More info",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

        },
        { Spacer(modifier = Modifier.size(8.dp)) },
        {
            Author(
                name = groupMembers[1],
                imageResId = R.drawable.gui,
                onSendEmailRequested = {
                    onSendEmail(ActionsIntent.Email(listOf(emails[0]), context))
                }
            )
            Author(
                name = groupMembers[0],
                imageResId = R.drawable.andre,
                onSendEmailRequested = {
                    onSendEmail(ActionsIntent.Email(listOf(emails[1]), context))
                }
            )
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