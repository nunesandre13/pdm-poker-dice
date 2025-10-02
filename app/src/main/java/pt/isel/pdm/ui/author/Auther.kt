package pt.isel.pdm.ui.author


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun Author(
    name: String,
    imageResId: Int,
    onSendEmailRequested: () -> Unit = { },
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onSendEmailRequested() }
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier
                .sizeIn(
                    minWidth = 50.dp, maxWidth = 100.dp,
                    minHeight = 50.dp, maxHeight = 100.dp
                )
                .clip(CircleShape)
        )
        Text(text = name, style = MaterialTheme.typography.bodyLarge)
        Icon(
            imageVector = Icons.Default.Email,
            contentDescription = null,
            modifier = Modifier.clickable { onSendEmailRequested() }
        )
    }
}