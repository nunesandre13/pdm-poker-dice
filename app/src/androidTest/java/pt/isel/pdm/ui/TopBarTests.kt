package pt.isel.pdm.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.ui.topBar.BACK_BUTTON_TAG
import pt.isel.pdm.ui.topBar.NEXT_BUTTON_TAG
import pt.isel.pdm.ui.topBar.TopBar
import pt.isel.pdm.ui.topBar.TopBarConfig

class TopBarTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun topBar_simple_Test() {
        composeTestRule.setContent {
            TopBar(
                TopBarConfig.WithBack(
                    title = "Testeeeeeeeeeeeeeeeeeeeeeeeeeee",
                    onBack = { }
                )
            )
        }

        composeTestRule.onNodeWithContentDescription(BACK_BUTTON_TAG).assertExists()
        composeTestRule.onNodeWithContentDescription(NEXT_BUTTON_TAG).assertDoesNotExist()
    }
}