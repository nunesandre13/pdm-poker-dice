package pt.isel.pdm.mainScreens.user

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.dto.user.UserCreateTokenInputModel
import pt.isel.pdm.ui.topBar.TopBarConfig
import pt.isel.pdm.user.ui.login.LoginScreen
import pt.isel.pdm.user.ui.login.ViewUserLoginStateFull

class ViewUserLoginStateFullTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun login_with_valid_credentials_triggers_onLogin() {
        var capturedUserLogin: UserCreateTokenInputModel? = null
        val expectedEmail = "test@example.com"
        val expectedPassword = "password123!"

        composeTestRule.setContent {
            ViewUserLoginStateFull(
                onLogin = { userLogin ->
                    capturedUserLogin = userLogin
                },
                onSignUp = {},
                loginView = { state, actions ->
                    LoginScreen(
                        topBarConfig = state.topBarConfig,
                        email = state.email,
                        password = state.password,
                        onEmailChange = actions.onEmailChange,
                        onPasswordChange = actions.onPasswordChange,
                        showPassword = state.showPassword,
                        onShowPassword = actions.onShowPassword,
                        login = actions.onLogin,
                        onSignUp = actions.onSignUp,
                        emailError = state.emailError,
                        passwordError = state.passwordError
                    )
                },
                topBarConfig = TopBarConfig.Simple("Login")
            )
        }

        composeTestRule.onNodeWithText("Email").performTextInput(expectedEmail)
        composeTestRule.onNodeWithText("Password").performTextInput(expectedPassword)
        composeTestRule.onNodeWithText("Sign in").performClick()

        assertNotNull("UserLogin object should not be null", capturedUserLogin)
        assertEquals(expectedEmail, capturedUserLogin?.email)
        assertEquals(expectedPassword, capturedUserLogin?.password)
    }
}
