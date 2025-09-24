package pt.isel.pdm.ui.row

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

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
