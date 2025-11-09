package pt.isel.pdm.game

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.ui.clickable.ClickableImage

@Composable
fun StoppedDice(face: DiceFace, onClick: () -> Unit, size: Dp) {
    ClickableImage(
        resourceId = face.resId,
        contentDescription = "Die face ${face.name}",
        onClick = onClick,
        size = size,
    )
}

@Composable
@Preview
fun PreviewDiceImage() {
    StoppedDice(
        DiceFace.ACE,
        onClick = {},
        size = Dp(200f)
    )
}

