package pt.isel.pdm.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.ui.ClickableImage

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



