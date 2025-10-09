package pt.isel.pdm.domain.inputs

import pt.isel.pdm.domain.Password

@JvmInline
value class PasswordInput(val passwordInput: String) {
    fun toPassword() : Password = Password(passwordInput)
}