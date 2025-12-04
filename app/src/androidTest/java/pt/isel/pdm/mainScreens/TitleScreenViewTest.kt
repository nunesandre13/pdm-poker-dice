package pt.isel.pdm.mainScreens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.home.TitleScreen

class TitleScreenViewTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun titleScreen_Test() {
        composeTestRule.setContent {
            TitleScreen(
                onAboutClick = {},
                onProfileClick = {},
                onStartMatchClick = {}
            )
        }
        composeTestRule.onNodeWithText("Start Match").assertExists()
        composeTestRule.onNodeWithText("About").assertExists()
    }
}