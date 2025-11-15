package pt.isel.pdm.match.ui.table

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
