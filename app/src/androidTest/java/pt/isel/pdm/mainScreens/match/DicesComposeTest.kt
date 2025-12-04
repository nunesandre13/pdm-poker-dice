package pt.isel.pdm.mainScreens.match

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.collections.immutable.toImmutableList
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand
import pt.isel.pdm.match.ui.dices.DisplayClickableDices
import pt.isel.pdm.match.ui.dices.DisplayStaticDices

class DicesComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleHand = DicesHand(
        dices = listOf(
            DiceFace.ACE,
            DiceFace.KING,
            DiceFace.QUEEN,
            DiceFace.JACK,
            DiceFace.NINE
        ).toImmutableList()
    )

    @Test
    fun displayStaticDices_showsFiveDice() {
        composeTestRule.setContent {
            DisplayStaticDices(dicesHand = sampleHand, size = 200.dp)
        }

        for (i in 0..4) {
            composeTestRule.onNodeWithTag("dice_$i").assertExists()
        }
    }

    @Test
    fun displayClickableDices_clickInvokesCallback() {
        val clicked = mutableListOf<DiceFace>()

        composeTestRule.setContent {
            DisplayClickableDices(
                dicesHand = sampleHand,
                onClick = { clicked.add(it) },
                size = 200.dp
            )
        }

        //clicar dado 2
        composeTestRule.onNodeWithTag("dice_2").performClick()

        Assert.assertEquals(1, clicked.size)
        Assert.assertEquals(DiceFace.QUEEN, clicked[0])
    }
}