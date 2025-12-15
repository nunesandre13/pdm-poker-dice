package pt.isel.pdm.dto.user

import kotlinx.serialization.Serializer

@Serializer
data class UserCreateTokenOutputModel(
    val token: String,
)