//package pt.isel.pdm.mainScreens.user
//
//import pt.isel.pdm.user.ui.create.CreateUserScreen
//import pt.isel.pdm.user.ui.create.ViewUserCreateStateFull
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.performClick
//import androidx.compose.ui.test.performTextInput
//import junit.framework.TestCase.assertEquals
//import org.junit.Assert.assertNotNull
//import org.junit.Rule
//import org.junit.Test
//import pt.isel.pdm.dto.user.UserInput
//import pt.isel.pdm.ui.topBar.TopBarConfig
//
//class ViewUserCreateStateFullTest {
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @Test
//    fun create_user_with_valid_input() {
//        var capturedUserCreate: UserInput? = null
//        val expectedName = "Test User"
//        val expectedEmail = "test@example.com"
//        val expectedPassword = "password123!"
//        val expectedInvite = "12345"
//
//        composeTestRule.setContent {
//            ViewUserCreateStateFull(
//                onCreateUser = { userCreate, _ ->
//                    capturedUserCreate = userCreate
//                },
//                createView = { state, actions ->
//                    CreateUserScreen(
//                        topBarConfig = state.topBarConfig,
//                        email = state.email,
//                        onEmailChange = actions.onEmailChange,
//                        userName = state.name,
//                        onUserNameChange = actions.onNameChange,
//                        password = state.password,
//                        onPasswordChange = actions.onPasswordChange,
//                        showPassword = state.showPassword,
//                        onShowPassword = actions.onShowPassword,
//                        onCreateUser = actions.onCreateUser,
//                        emailError = state.emailError,
//                        passwordError = state.passwordError,
//                        // Adicionei o inviteCode que faltava no teu snippet anterior se necessário
//                        inviteCode = state.inviteCode
//                    )
//                },
//                topBarConfig = TopBarConfig.Simple("Create User")
//            )
//        }
//
//        // 1. Preencher Nome (Clica primeiro para garantir foco)
//        composeTestRule.onNodeWithText("Name").performClick().performTextInput(expectedName)
//
//        // 2. Preencher Email
//        composeTestRule.onNodeWithText("Email").performClick().performTextInput(expectedEmail)
//
//        // 3. Preencher Password
//        composeTestRule.onNodeWithText("Password").performClick().performTextInput(expectedPassword)
//
//        // 4. Se houver um campo de Invite Code no teu ecrã, tens de preenchê-lo!
//        // No teu viewUserCreateStateFull.kt, o botão só dispara se inviteCode != null
//        composeTestRule.onNodeWithText("Invite Code").performTextInput(expectedInvite)
//
//        // 5. Clicar no botão
//        composeTestRule.onNodeWithText("Create Account").performClick()
//
//        // Aguarda até que a thread principal esteja ociosa (importante para coroutines)
//        composeTestRule.waitForIdle()
//
//        assertNotNull("O objeto UserInput não foi capturado", capturedUserCreate)
//        assertEquals(expectedName, capturedUserCreate?.name)
//        assertEquals(expectedEmail, capturedUserCreate?.email)
//        assertEquals(expectedPassword, capturedUserCreate?.password)
//    }
//}
