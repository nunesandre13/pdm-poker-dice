package pt.isel.pdm.ui.topBar

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.ui.clickable.ClickableIcon

const val BACK_BUTTON_TAG = "Back"
const val NEXT_BUTTON_TAG = "Next"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(config: TopBarConfig) {
    TopAppBar(
        modifier = Modifier.height(56.dp),
        title = {
            Text(
                text = config.title,
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            when (config) {
                is TopBarConfig.WithBack -> {
                    ClickableIcon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = BACK_BUTTON_TAG) {
                        config.onBack()
                    }
                }
                is TopBarConfig.WithBackAndNext -> {
                    ClickableIcon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = BACK_BUTTON_TAG) {
                        config.onBack()
                    }
                }
                else -> {}
            }
        },
        actions = {
            when (config) {
                is TopBarConfig.WithNext -> {
                    ClickableIcon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = NEXT_BUTTON_TAG) {
                        config.onNext()
                    }
                }
                is TopBarConfig.WithBackAndNext -> {
                    ClickableIcon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = NEXT_BUTTON_TAG) {
                        config.onNext()
                    }
                }
                else -> {}
            }
           IsProfile(config)
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF676C7D),
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        scrollBehavior = null
    )
}

@Composable
fun IsProfile(config: TopBarConfig) {
    if (config is TopBarConfig.Profile) {
            ClickableIcon(Icons.Filled.AccountCircle, contentDescription = "Profile") {
               config.onProfileClick()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar(
        TopBarConfig.WithBackNextAndProfile(
            title = "Exemplo",
            onBack = {},
            onNext = {},
            onProfileClick = {}
        )
    )
}
