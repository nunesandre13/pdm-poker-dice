package pt.isel.pdm.ui.topBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.ui.clickable.ClickableIcon

const val BACK_BUTTON_TAG = "Back"
const val NEXT_BUTTON_TAG = "Next"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(config: TopBarConfig) {
    TopAppBar(
        title = { Text(config.title) },
        navigationIcon = {
            when (config) {
                is TopBarConfig.WithBack -> {
                    ClickableIcon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = BACK_BUTTON_TAG){
                        config.onBack()
                    }
                }
                is TopBarConfig.WithBackAndNext -> {
                    ClickableIcon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = BACK_BUTTON_TAG){
                        config.onBack()
                    }
                }
                else -> {}
            }
        },
        actions = {
            when (config) {
                is TopBarConfig.WithNext -> {
                    ClickableIcon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = NEXT_BUTTON_TAG){
                        config.onNext()
                    }
                }
                is TopBarConfig.WithBackAndNext -> {
                    ClickableIcon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = NEXT_BUTTON_TAG){
                        config.onNext()
                    }
                }
                else -> {}
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar(
        TopBarConfig.WithBack(
            title = "Exemplo",
            onBack = {}
        )
    )
}
