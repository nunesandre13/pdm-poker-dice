package pt.isel.pdm.mainScreens.match

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.match.ui.playerLayouts.PlaceOtherPlayers

class PlaceOtherPlayersTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun placeOtherPlayers_renders_all_players() {

        val players = listOf("Player 1", "Player 2", "Player 3")
        val playerComposable: PlayerComposable<String> = { player, modifier ->
            Text(text = player, modifier = modifier)
        }


        val constraintBlocks: List<ConstrainScope.(List<ConstrainedLayoutReference>) -> Unit> =
            players.map { { _ -> } }

        composeTestRule.setContent {
            ConstraintLayout {
                PlaceOtherPlayers(
                    players = players,
                    constraintBlocks = constraintBlocks,
                    playersComposable = playerComposable
                )
            }
        }

        players.forEach { player ->
            composeTestRule.onNodeWithText(player).assertIsDisplayed()
        }
    }
}
