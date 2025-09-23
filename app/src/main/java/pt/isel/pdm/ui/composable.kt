package pt.isel.pdm.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pt.isel.pdm.ui.Handlers.TopBarConfig

@Composable
fun ColumnScaffold(topBar : (@Composable () -> Unit)? = null,  vararg content: @Composable () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { topBar?.invoke() }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            content.forEach { composable -> composable() }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(config: TopBarConfig) {
    TopAppBar(
        title = { Text(config.title) },
        navigationIcon = {
            when (config) {
                is TopBarConfig.WithBack -> {
                    ClickableIcon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"){
                        config.onBack()
                    }
                }
                is TopBarConfig.WithBackAndNext -> {
                    ClickableIcon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"){
                        config.onBack()
                    }
                }
                else -> {}
            }
        },
        actions = {
            when (config) {
                is TopBarConfig.WithNext -> {
                    ClickableIcon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next"){
                        config.onNext()
                    }
                }
                is TopBarConfig.WithBackAndNext -> {
                    ClickableIcon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next"){
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultBackGround(
    vararg content: @Composable () -> Unit,
    topBarConfig : TopBarConfig,
){
    ColumnScaffold(topBar = { TopBar(topBarConfig) }, content= content,)
}



@Composable
fun ClickableIcon(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}


@Composable
fun ClickableImage(
    resourceId: Int,
    contentDescription: String? = null,
    onClick: () -> Unit,
    size: Dp,
) {
    Image(
        painter = painterResource(id = resourceId),
        contentDescription = contentDescription,
        modifier = Modifier.clickable { onClick() }
            .size(size)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultBackGroundPreview() {
    DefaultBackGround(topBarConfig = TopBarConfig.Simple("Exemplo"))
}

@Composable
fun MakeRow(
    vararg content: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        content.forEach {
            composable -> composable()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MakeRowPreview() {
    MakeRow(
        {
            Text(
                text = "Item 1",
                style = MaterialTheme.typography.bodyLarge
            )
        },
        {
            Text(
                text = "Item 2",
                style = MaterialTheme.typography.bodyLarge
            )
        },
        {
            Text(
                text = "Item 3",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    )
}
