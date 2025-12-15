package pt.isel.pdm.configuration

import android.app.Application
import android.util.Log
import pt.isel.pdm.lobby.repository.RepositoryLobbies
import pt.isel.pdm.lobby.repository.RepositoryLobbiesMock
import pt.isel.pdm.lobby.services.LobbyServiceImp
import pt.isel.pdm.match.repository.RepositoryMatch
import pt.isel.pdm.match.repository.RepositoryMatchMock
import pt.isel.pdm.match.services.MatchServiceImp
import pt.isel.pdm.match.services.MatchServices
import pt.isel.pdm.user.services.UserServicesHttp
import pt.isel.pdm.user.services.UsersServiceMock


class MockConfiguration : Application(), DependenciesContainer {


    init {
        Log.d("Mock", "Mock configuration created")
    }

    override val repoLobby: RepositoryLobbies by lazy {
        RepositoryLobbiesMock()
    }

    override val userServices by lazy{
        UserServicesHttp()
    }

    override val lobbyServices by lazy{
        LobbyServiceImp(repoLobby)
    }

    override val matchRepo: RepositoryMatch by lazy {
        RepositoryMatchMock()
    }

    override val matchService: MatchServices by lazy {
        MatchServiceImp(matchRepo)
    }

}