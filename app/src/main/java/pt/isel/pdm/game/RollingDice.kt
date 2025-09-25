package pt.isel.pdm.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.ui.clickable.ClickableImage

@Composable
fun RollingDie(
    dices: List<DiceFace>,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            dices.forEach { face ->
                DiceImage(
                    face = face, {}, 64.dp
                )
            }
        }
    }
}

@Composable
fun DiceImage(face: DiceFace, onClick: () -> Unit, size: Dp) {
    ClickableImage(
        resourceId = face.resId,
        contentDescription = "Die face ${face.name}",
        onClick = onClick,
        size = size,
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun RollingDiePreview() {
    RollingDie(
        dices = listOf(
            DiceFace.ACE, DiceFace.KING, DiceFace.QUEEN,
            DiceFace.JACK, DiceFace.TEN, DiceFace.NINE
        )
    )
}

