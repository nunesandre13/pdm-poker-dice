package pt.isel.pdm.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class InvitationUrlModel(
    val url: String
)