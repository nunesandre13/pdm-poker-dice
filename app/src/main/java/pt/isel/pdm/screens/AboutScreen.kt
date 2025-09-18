package pt.isel.pdm.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.chelasmulti_playerpokerdice.R

@Composable
fun AboutScreen() {
    val context = LocalContext.current

    val groupMembers = listOf("André Nunes - 51766", "Guilherme Coutinho - 50467")
    val emails = listOf("A51766@alunos.isel.pt","A50467@alunos.isel.pt")

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                text = "Detailed description",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    fetchAboutOnBrowser(context, "https://testeeeee")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Members of group:", style = MaterialTheme.typography.titleMedium)
            groupMembers.forEach { member ->
                Text(text = member)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                fetchMembers(
                    context = context,
                    emails = emails
                )
            }) {
                Text("Contact all members")
            }
        }
    }
}

fun fetchMembers(
    context: Context,
    emails: List<String>
) {
    val intent = Intent(Intent.ACTION_SENDTO,"mailto:".toUri() ).apply {
        putExtra(Intent.EXTRA_EMAIL, emails.toTypedArray())
    }
    try {
        context.startActivity(Intent.createChooser(intent, "Escolher app de email"))
    } catch (e: ActivityNotFoundException) {

    }
}

fun fetchAboutOnBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {

    }
}

@Preview(showBackground = true)
@Composable
fun Teste(){
    AboutScreen()
}