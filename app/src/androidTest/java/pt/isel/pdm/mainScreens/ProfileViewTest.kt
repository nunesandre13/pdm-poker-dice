package pt.isel.pdm.mainScreens

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertTrue
import pt.isel.pdm.domain.user.User
import pt.isel.pdm.domain.user.Email
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.UserId
import pt.isel.pdm.profile.ui.ProfileView

class ProfileViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val dummyUser = User(
        id = UserId(1),
        name = Name("Player"),
        email = Email("player@example.com")
    )

    @Test
    fun profileViewDisplayTest() {
        var logoutClicked = false

        composeTestRule.setContent {
            ProfileView(
                user = dummyUser,
                onBack = {},
                onLogOut = { logoutClicked = true }
            )
        }

        composeTestRule.onAllNodesWithText("Player").assertCountEquals(2)
        composeTestRule.onAllNodesWithText("Player")[0].assertIsDisplayed()
        composeTestRule.onNodeWithText("player@example.com").assertIsDisplayed()
        composeTestRule.onNodeWithText("Statistics").assertIsDisplayed()
        composeTestRule.onNodeWithText("P").assertIsDisplayed()
        composeTestRule.onNodeWithText("LogOut").assertIsDisplayed()
        composeTestRule.onNodeWithText("LogOut").performClick()
        assertTrue(logoutClicked)
    }
}
