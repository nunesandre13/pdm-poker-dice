package pt.isel.pdm.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pt.isel.pdm.domain.User
import pt.isel.pdm.ui.ProfileCard
import pt.isel.pdm.ui.StatCard
import pt.isel.pdm.ui.background.DefaultBackGround
import pt.isel.pdm.ui.topBar.TopBarConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(
    user: User,
    onBack: () -> Unit = {},
    onLogOut:() -> Unit = {}
) {
    DefaultBackGround(
        {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.name.first().uppercase(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }
                Text(text = user.name, style = MaterialTheme.typography.titleLarge)


                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProfileCard(
                        icon = { Icon(Icons.Default.Person, contentDescription = "Name") },
                        title = "Name",
                        value = user.name,
                        modifier = Modifier.fillMaxWidth()
                    )
                    ProfileCard(
                        icon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                        title = "Email",
                        value = user.email.email,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(
                    text = "Statistics",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard("Games", "24")
                    StatCard("Wins", "12")
                    StatCard("Rank", "#5")
                }
                Button(
                    onLogOut
                ) {
                    Text("LogOut")
                }
            }
        },
        topBarConfig = TopBarConfig.WithBack(
            title = "Profile",
            onBack = onBack
        ),
        modifier = Modifier.fillMaxSize()
    )
}

