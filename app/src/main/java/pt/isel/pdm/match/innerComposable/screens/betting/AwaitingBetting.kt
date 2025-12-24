package pt.isel.pdm.match.innerComposable.screens.betting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AwatingBetting(onCall: () -> Unit, onFold: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onCall) {
                Text("Call")
            }
            Button(onClick = onFold) {
                Text("Fold")
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode", widthDp = 360, heightDp = 640)
@Composable
fun AwatingBettingPreview() {
    MaterialTheme {
        Surface {
            AwatingBetting(
                onCall = {},
                onFold = {}
            )
        }
    }
}
