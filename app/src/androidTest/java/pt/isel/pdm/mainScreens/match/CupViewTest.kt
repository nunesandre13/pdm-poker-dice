package pt.isel.pdm.mainScreens.match

import pt.isel.pdm.match.ui.Cup

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.IntSize
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CupViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun cup_is_displayed() {

        composeTestRule.setContent {
            Cup(
                offsetX = 0f,
                offsetY = 0f,
                rotation = 0f,
                onCupSized = { },
                enable = true,
                onClick = { }
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Dice cup")
            .assertIsDisplayed()
    }

    @Test
    fun cup_onClick_is_called() {
        var clicked = false
        composeTestRule.setContent {
            Cup(
                offsetX = 0f,
                offsetY = 0f,
                rotation = 0f,
                onCupSized = { },
                enable = true,
                onClick = { clicked = true }
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Dice cup")
            .performClick()


        assertTrue(clicked)
    }

    @Test
    fun cup_onCupSized_is_called() {

        var cupSize: IntSize? = null
        composeTestRule.setContent {
            Cup(
                offsetX = 0f,
                offsetY = 0f,
                rotation = 0f,
                onCupSized = { cupSize = it },
                onClick = { },
                enable = true
            )
        }
        composeTestRule.waitForIdle()


        assertNotNull(cupSize)
        assertTrue(cupSize!!.width > 0)
        assertTrue(cupSize.height > 0)
    }
}
