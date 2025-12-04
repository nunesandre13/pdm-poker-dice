package pt.isel.pdm.mainScreens.match

import pt.isel.pdm.match.ui.playerView.BasePlayerView
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class BasePlayerViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun basePlayerView_displays_content_correctly() {
        val contentText = "Test Content"
        composeTestRule.setContent {
            BasePlayerView {
                Text(text = contentText)
            }
        }

        composeTestRule
            .onNodeWithText(contentText)
            .assertIsDisplayed()
    }
}
