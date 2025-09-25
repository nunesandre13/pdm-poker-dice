package pt.isel.pdm.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DiceState
import pt.isel.pdm.domain.DicesHandState
import pt.isel.pdm.ui.column.ColumnScaffold

@Composable
fun DiceHandScreen(initialHand: DicesHandState, diceSize: Dp) {
    var dicesHand by remember { mutableStateOf(initialHand) }

    ColumnScaffold(content =  dicesHand.dices.map { diceState ->
            if (diceState.isRolling) {
                @Composable{
                    RollingDie(diceState.face, {}, diceSize)
                }
            } else {
                {
                    StopedDice(diceState.face, {}, diceSize)
                }
            }
        }.toTypedArray()
    )
}



@Composable
@Preview
fun DiceHandScreenPreview() {
    DiceHandScreen(
        initialHand = DicesHandState(
            dices = DiceFace.entries.mapIndexed { i, face ->
                DiceState(face, isRolling = i % 2 != 0)
            }
        ), 64.dp
    )
}