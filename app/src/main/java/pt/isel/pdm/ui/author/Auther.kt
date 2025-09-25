package pt.isel.pdm.ui.author

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chelasmulti_playerpokerdice.R




@Composable
fun Author(onSendEmailRequested: () -> Unit = { }) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onSendEmailRequested() }
    ) {
        Image(
            painter = painterResource(id = R.drawable.foto),
            contentDescription = null,
            modifier = Modifier.sizeIn(
                minWidth = 100.dp, maxWidth = 200.dp,
                minHeight = 100.dp, maxHeight = 200.dp
            )
        )
        Text(text = "Guilherme Coutinho", style = MaterialTheme.typography.titleLarge)

    }
}