package pt.isel.pdm.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.delay
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.ui.clickable.ClickableImage

@Composable
fun StopedDice(face: DiceFace, onClick: () -> Unit, size: Dp) {
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
    StopedDice(
        DiceFace.ACE,
        onClick = {},
        size = Dp(200f)
    )
}

@Composable
fun RollingDie(
    face: DiceFace,
    onRollEnd: (DiceFace) -> Unit,
    size: Dp
) {
    var currentFace by remember { mutableStateOf(face) }

    LaunchedEffect(null) {
            repeat(10) {
                currentFace = DiceFace.entries.random()
                delay(70)
            }
            onRollEnd(currentFace)
    }
    Image(
        painter = painterResource(id = currentFace.resId),
        contentDescription = currentFace.name,
        modifier = Modifier.size(size)
    )
}

@Composable
@Preview
fun PreviewRollingDice(){
    RollingDie(DiceFace.ACE, onRollEnd = {}
    , size = Dp(200f))
}
