package pt.isel.pdm.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.ui.background.DefaultBackGround
import pt.isel.pdm.ui.topBar.BACK_BUTTON_TAG
import pt.isel.pdm.ui.topBar.TopBarConfig

@Composable
fun HandlingView(
    onBack: () -> Unit = {}
) {
    DefaultBackGround(
        {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp)
                )
            }
        },
        topBarConfig = TopBarConfig.WithBack(
            title = "Game is starting...",
            onBack = onBack
        ),
        modifier = Modifier
    )

}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun HandlingViewPreview() {
    HandlingView({})

}