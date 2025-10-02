package pt.isel.pdm.configuration

import pt.isel.pdm.lobby.services.LobbyServices
import pt.isel.pdm.user.services.UserServices

interface DependenciesContainer {
    val userServices: UserServices
    val lobbyServices: LobbyServices
}
