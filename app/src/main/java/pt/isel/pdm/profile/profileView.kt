package pt.isel.pdm.profile

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.User
import pt.isel.pdm.ui.background.DefaultBackGround
import pt.isel.pdm.ui.topBar.TopBarConfig


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User, onBack: () -> Unit = {}
) {
    DefaultBackGround({
            Text("Player Info and Statistics")
            Text("Name: ${user.name}")
            Text("Email: ${user.id}")
        },
        topBarConfig = TopBarConfig.WithBack(
            title = "Profile",
            onBack = onBack
        ),
        modifier = Modifier
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    ProfileScreen(User("joao","12344")) {}
}
