package pt.isel.pdm.game.ui.dices

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.ui.clickable.ClickableImage

@Composable
fun ClickableDice(face: DiceFace, onClick: () -> Unit, size: Dp) {
    ClickableImage(
        resourceId = face.resId,
        contentDescription = "Die face ${face.name}",
        onClick = onClick,
        size = size,
    )
}

@Composable
fun StaticDice(
    face: DiceFace,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = face.resId),
        contentDescription = "Die face ${face.name}",
        modifier = modifier.size(size)
    )
}

@Composable
@Preview
fun PreviewStaticDiceImage() {
    StaticDice(
        DiceFace.ACE,
        size = Dp(200f)
    )
}

