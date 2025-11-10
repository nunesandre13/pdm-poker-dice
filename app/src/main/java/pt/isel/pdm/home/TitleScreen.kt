package pt.isel.pdm.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
                                Color(0xFF0B6623),
                                Color(0xFF002200)
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
                            .size(160.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "♠ Poker Dice ♣",
                        fontSize = 32.sp,
                        color = Color(0xFFFFD700),
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF121212).copy(alpha = 0.85f)
                        ),
                        modifier = Modifier.fillMaxWidth(0.85f)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            PokerButton(
                                text = "🎲 Start Match",
                                onClick = onStartMatchClick,
                                background = Brush.horizontalGradient(
                                    listOf(Color(0xFFD4AF37), Color(0xFFFFD700))
                                ),
                                textColor = Color.Black
                            )
                            PokerButton(
                                text = "ℹ️ About",
                                onClick = onAboutClick,
                                background = Brush.horizontalGradient(
                                    listOf(Color(0xFFB22222), Color(0xFF8B0000))
                                )
                            )
                        }
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

@Composable
fun PokerButton(
    text: String,
    onClick: () -> Unit,
    background: Brush,
    textColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .background(background, RoundedCornerShape(16.dp))
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TitlePreview() {
    TitleScreen({}, {}, {})
}
