package pt.isel.pdm.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chelasmulti_playerpokerdice.R
import pt.isel.pdm.ui.background.DefaultBackGround
import pt.isel.pdm.ui.topBar.TopBarConfig

@Composable
fun TitleScreen(
    onAboutClick: () -> Unit,
    onProfileClick: () -> Unit,
    onStartMatchClick: () -> Unit
) {
    DefaultBackGround(
        {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.85f)
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.background),
                        contentDescription = null,
                        modifier = Modifier
                            .size(180.dp)
                            .shadow(12.dp, CircleShape)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = stringResource(id = R.string.title_app),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    )

                    OutlinedButton(
                        onClick = onStartMatchClick,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(vertical = 8.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text("Start Match", fontSize = 18.sp)
                    }

                    OutlinedButton(
                        onClick = onAboutClick,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(vertical = 8.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text("About", fontSize = 18.sp)
                    }
                }
            }
        },
        topBarConfig = TopBarConfig.WithProfile(
            title = "Poker Dice",
            onProfileClick = onProfileClick
        ),
        modifier = Modifier.fillMaxSize()
    )
}


@Preview(showBackground = true)
@Composable
fun TitlePreview() {
    TitleScreen({}, {}, {})
}
