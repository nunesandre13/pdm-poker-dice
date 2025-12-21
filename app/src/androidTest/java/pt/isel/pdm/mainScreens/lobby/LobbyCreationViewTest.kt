package pt.isel.pdm.mainScreens.lobby

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.domain.LobbyCreation
import pt.isel.pdm.lobby.ui.LobbyCreationView

class LobbyCreationViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun createLobby_calls_callback_with_correct_data() {
        var capturedLobby: LobbyCreation? = null
        composeTestRule.setContent {
            LobbyCreationView(
                onCreateLobby = { capturedLobby = it },
                onBack = {}
            )
        }

        val textFields = composeTestRule.onAllNodes(hasSetTextAction())

        textFields[0].performTextInput("Sala de Teste")
        textFields[1].performTextInput("Descricao do lobby")
        textFields[2].performTextInput("2")
        textFields[3].performTextInput("4")
        textFields[4].performTextInput("5")
        textFields[5].performTextInput("10")

        composeTestRule.onNode(hasText("Create Lobby") and hasClickAction()).performClick()

        assert(capturedLobby != null)
        assert(capturedLobby?.name == "Sala de Teste")
        assert(capturedLobby?.minPlayer == 2)
        assert(capturedLobby?.firstAnte == 10)
    }

    @Test
    fun back_button_triggers_onBack() {
        var backClicked = false
        composeTestRule.setContent {
            LobbyCreationView(
                onCreateLobby = {},
                onBack = { backClicked = true }
            )
        }
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        assert(backClicked)
    }
}
