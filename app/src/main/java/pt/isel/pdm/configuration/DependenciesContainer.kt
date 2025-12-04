package pt.isel.pdm.configuration

import pt.isel.pdm.lobby.repository.RepositoryLobbies
import pt.isel.pdm.lobby.services.LobbyServices
import pt.isel.pdm.match.repository.RepositoryMatch
import pt.isel.pdm.match.services.MatchServices
import pt.isel.pdm.user.services.UserServices

interface DependenciesContainer {

    val repoLobby : RepositoryLobbies
    val userServices: UserServices
    val lobbyServices: LobbyServices
    val matchRepo: RepositoryMatch
    val matchService : MatchServices

}
