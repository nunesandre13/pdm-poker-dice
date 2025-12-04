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
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.Password
import pt.isel.pdm.domain.UserCreate
import pt.isel.pdm.ui.topBar.TopBarConfig

class ViewUserCreateStateFullTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun create_user_with_valid_input() {

        var capturedUserCreate: UserCreate? = null
        val expectedName = "Test User"
        val expectedEmail = "test@example.com"
        val expectedPassword = "password123!"

        composeTestRule.setContent {
            ViewUserCreateStateFull(
                onCreateUser = { userCreate ->
                    capturedUserCreate = userCreate
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
                        passwordError = state.passwordError
                    )
                },
                topBarConfig = TopBarConfig.Simple("Create User")
            )
        }

        composeTestRule.onNodeWithText("Name").performTextInput(expectedName)
        composeTestRule.onNodeWithText("Email").performTextInput(expectedEmail)
        composeTestRule.onNodeWithText("Password").performTextInput(expectedPassword)
        composeTestRule.onNodeWithText("Create Account").performClick()


        assertNotNull(capturedUserCreate)
        assertEquals(Name(expectedName), capturedUserCreate?.name)
        assertEquals(Email(expectedEmail), capturedUserCreate?.email)
        assertEquals(Password(expectedPassword), capturedUserCreate?.password)
    }
}
