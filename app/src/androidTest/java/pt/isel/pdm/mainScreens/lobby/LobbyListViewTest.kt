package pt.isel.pdm.mainScreens.lobby

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.LobbyId
import pt.isel.pdm.domain.LobbyStatus
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.UserId
import pt.isel.pdm.lobby.ui.LobbyListView

class LobbyListViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun lobbyListViewTest() {
        var joinedLobby: Lobby? = null
        var createClicked = false
        var backClicked = false

        val players = listOf(
            User(UserId(1), Name("Alice"), Email("alice@mail"))
        )

        val lobbyA = Lobby(
            id = LobbyId(2),
            name = "Lobby A",
            description = "desc A",
            players = players,
            owner = UserId(1),
            maxPlayer = 4,
            minPlayer = 1,
            numberOdRounds = 3,
            firstAnte = 5,
            matchId = null,
            lobbyStatus = LobbyStatus.OPEN
        )

        val lobbyB = Lobby(
            id = LobbyId(3),
            name = "Lobby B",
            description = "desc B",
            players = players,
            owner = UserId(1),
            maxPlayer = 6,
            minPlayer = 2,
            numberOdRounds = 4,
            firstAnte = 10,
            matchId = null,
            lobbyStatus = LobbyStatus.OPEN
        )


        composeTestRule.setContent {
            LobbyListView(
                lobbies = listOf(lobbyA, lobbyB),
                onJoinClick = { joinedLobby = it },
                onBack = { backClicked = true },
                onCreateLobby = { createClicked = true }
            )
        }

        composeTestRule.onAllNodes(hasText("Join")).assertCountEquals(2)
        composeTestRule.onAllNodes(hasText("Join") and hasClickAction()).get(0).performClick()
        assert(joinedLobby?.id?.id == lobbyA.id.id)


        composeTestRule.onNode(hasText("Create Lobby") and hasClickAction()).performClick()
        assert(createClicked)
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        assert(backClicked)
    }
}
