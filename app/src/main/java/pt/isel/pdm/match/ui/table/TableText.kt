package pt.isel.pdm.match.ui.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chelasmulti_playerpokerdice.R

@Composable
fun TableTitle(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.table_title),
        fontSize = 38.sp,
        fontWeight = FontWeight.Black,
        color = colorResource(R.color.title_gold),
        style = TextStyle(
            shadow = Shadow(
                color = Color.Black.copy(alpha = 0.6f),
                offset = Offset(3f, 3f),
                blurRadius = 10f
            )
        ),
        modifier = modifier
    )
}


@Preview(showBackground = true, name = "Table Title Dark Background")
@Composable
fun TableTitlePreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF1B5E20))
            .padding(32.dp)
    ) {
        TableTitle()
    }
}