package pt.isel.pdm.profile.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.chelasmulti_playerpokerdice.R
import pt.isel.pdm.domain.user.User
import pt.isel.pdm.ui.ProfileCard
import pt.isel.pdm.ui.background.DefaultBackGround
import pt.isel.pdm.ui.topBar.TopBarConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(
    user: User,
    inviteCode: String? = null,
    onBack: () -> Unit = {},
    onLogOut:() -> Unit = {},
    onGenerateInviteCode: () -> Unit = {},
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
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
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.name.name.first().uppercase(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }
                Text(text = user.name.name, style = MaterialTheme.typography.titleLarge)


                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProfileCard(
                        icon = { Icon(Icons.Default.Person, contentDescription =  stringResource(R.string.nameProfile)) },
                        title = stringResource(R.string.nameProfile),
                        value = user.name.name,
                        modifier = Modifier.fillMaxWidth()
                    )
                    ProfileCard(
                        icon = { Icon(Icons.Default.Email, contentDescription = stringResource(R.string.emailProfile)) },
                        title =  stringResource(R.string.emailProfile),
                        value = user.email.email,
                        modifier = Modifier.fillMaxWidth()
                    )
                    inviteCode?.let { code ->
                        val stringClick = stringResource(R.string.copied)
                        ProfileCard(
                            icon = { Icon(Icons.Default.Share, contentDescription = stringResource(R.string.inviteCode)) },
                            title = stringResource(R.string.copyInvite),
                            value = code,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    clipboardManager.setText(AnnotatedString(code))
                                    Toast.makeText(context, stringClick, Toast.LENGTH_SHORT).show()
                                }
                        )
                    }
                }

                Button(
                    onClick = onGenerateInviteCode,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(stringResource(R.string.generate))
                }
                Button(
                    onClick = onLogOut,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(stringResource(R.string.logout))
                }
            }
        },
        topBarConfig = TopBarConfig.WithBack(
            title = stringResource(R.string.profile),
            onBack = onBack
        ),
        modifier = Modifier.fillMaxSize()
    )
}

