package pt.isel.pdm.mainScreens.lobby

import pt.isel.pdm.lobby.ui.LobbyView
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.domain.user.Email
import pt.isel.pdm.domain.lobby.Lobby
import pt.isel.pdm.domain.LobbyId
import pt.isel.pdm.domain.lobby.LobbyStatus
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.user.User
import pt.isel.pdm.domain.UserId
import pt.isel.pdm.domain.user.toPlayerInfo

class LobbyViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun lobbyViewTest() {
        var left = false
        val players = listOf(User(UserId(1), Name("Alice"), Email("alice@mail")).toPlayerInfo(), User(UserId(2), Name("Bob"), Email("bob@mail")).toPlayerInfo())
        val lobby = Lobby(id = LobbyId(2), name = "Test Lobby", description = "desc", players = players, owner = UserId(1), maxPlayer = 4, minPlayer = 2, numberOdRounds = 3, firstAnte = 10, matchId = null, lobbyStatus = LobbyStatus.OPEN)

        composeTestRule.setContent {
            LobbyView(lobby = lobby, onLeave = { left = true })
        }

        composeTestRule.onNodeWithText("Test Lobby").assertIsDisplayed()
        composeTestRule.onNodeWithText("Máx jogadores: 4 | Actual Players: 2").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Leave").performClick()
        assert(left)
    }
}
