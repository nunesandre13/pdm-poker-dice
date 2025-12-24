package pt.isel.pdm.mainScreens.user

import pt.isel.pdm.user.ui.create.CreateUserScreen
import pt.isel.pdm.user.ui.create.ViewUserCreateStateFull
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.domain.user.InviteCode
import pt.isel.pdm.dto.user.UserInput
import pt.isel.pdm.ui.topBar.TopBarConfig

class ViewUserCreateStateFullTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun create_user_with_valid_input() {
        var capturedUserCreate: UserInput? = null
        var capturedInviteCode: InviteCode? = null
        val expectedName = "Test User"
        val expectedEmail = "test@example.com"
        val expectedPassword = "password123!"
        val expectedInvite = "12345"

        composeTestRule.setContent {
            ViewUserCreateStateFull(
                onCreateUser = { userCreate, invite ->
                    capturedUserCreate = userCreate
                    capturedInviteCode = invite
                },
                createView = { state, actions ->
                    CreateUserScreen(
                        topBarConfig = state.topBarConfig,
                        email = state.email,
                        onEmailChange = actions.onEmailChange,
                        userName = state.name,
                        onUserNameChange = actions.onNameChange,
                        password = state.password,
                        onPasswordChange = actions.onPasswordChange,
                        showPassword = state.showPassword,
                        onShowPassword = actions.onShowPassword,
                        onCreateUser = actions.onCreateUser,
                        emailError = state.emailError,
                        passwordError = state.passwordError,
                        inviteCode = state.inviteCode,
                        onInviteChange = actions.onInviteCodeChange
                    )
                },
                topBarConfig = TopBarConfig.Simple("Create User")
            )
        }


        composeTestRule.onNodeWithText("Name").performTextInput(expectedName)
        composeTestRule.onNodeWithText("Email").performTextInput(expectedEmail)
        composeTestRule.onNodeWithText("Invite").performTextInput(expectedInvite)
        composeTestRule.onNodeWithText("Password").performTextInput(expectedPassword)

        composeTestRule.onNodeWithText("Create Account").performClick()

        composeTestRule.waitForIdle()

        // Asserções
        assertNotNull("O UserInput não foi capturado!", capturedUserCreate)
        assertEquals(expectedName, capturedUserCreate?.name)
        assertEquals(expectedEmail, capturedUserCreate?.email)
        assertEquals(expectedInvite, capturedInviteCode?.code)
    }
}

