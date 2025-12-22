package pt.isel.pdm.domain

import pt.isel.pdm.domain.inputs.EmailInput
import pt.isel.pdm.domain.inputs.InviteInput
import pt.isel.pdm.domain.inputs.NameInput
import pt.isel.pdm.domain.inputs.PasswordInput

fun EmailInput.toEmail(): Email? =
    if (email.isNotBlank() && email.contains("@")) Email(email) else null

fun NameInput.toName(): Name? =
    if (name.isNotBlank() ) Name(name) else null


fun PasswordInput.toPassword(): Password? = Password(passwordInput)
//    if (passwordInput.isNotBlank() && passwordInput.length >= 5 && passwordInput.any { it in Password.specialChars }) Password(passwordInput) else null


fun InviteInput.toInviteCode() : InviteCode? =
    if (invite.isNotBlank()) InviteCode(invite) else null



