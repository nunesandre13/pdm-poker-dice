package pt.isel.pdm.mainScreens.match

import pt.isel.pdm.match.ui.playerLayouts.MakeLayout
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test


typealias PlayerComposable<T> = @Composable (player: T, modifier: Modifier) -> Unit

class MakeLayoutTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun makeLayout_with_one_other_players() {
        val me = "Me"
        val others = listOf("Player 2")
        setupAndAssert(me, others)
    }

    @Test
    fun makeLayout_with_three_other_players() {
        val me = "Me"
        val others = listOf("Player 2", "Player 3", "Player 4")
        setupAndAssert(me, others)
    }

    @Test
    fun makeLayout_with_five_other_players() {
        val me = "Me"
        val others = listOf("Player 2", "Player 3", "Player 4", "Player 5", "Player 6")
        setupAndAssert(me, others)
    }

    private fun setupAndAssert(me: String, others: List<String>) {
        val myPlayerComposable: PlayerComposable<String> = { player, modifier ->
            Text(text = player, modifier = modifier)
        }
        val otherPlayersComposable: PlayerComposable<String> = { player, modifier ->
            Text(text = player, modifier = modifier)
        }

        composeTestRule.setContent {
            MakeLayout(
                me = me,
                others = others,
                myPlayerComposable = myPlayerComposable,
                otherPlayersComposable = otherPlayersComposable
            )
        }

        composeTestRule.onNodeWithText(me).assertIsDisplayed()
        others.forEach { player ->
            composeTestRule.onNodeWithText(player).assertIsDisplayed()
        }
    }
}
