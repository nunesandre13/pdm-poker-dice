package pt.isel.pdm.configuration

import android.app.Application
import android.util.Log
import pt.isel.pdm.lobby.repository.RepositoryLobbies
import pt.isel.pdm.lobby.repository.RepositoryLobbiesMock
import pt.isel.pdm.lobby.services.LobbyServiceImp
import pt.isel.pdm.user.services.UsersServiceMock


// podia ser um singleton
class MockConfiguration : Application(), DependenciesContainer {


    init {
        Log.d("Mock", "Mock configuration created")
    }

    override val repoLobby: RepositoryLobbies by lazy {
        RepositoryLobbiesMock()
    }

    override val userServices by lazy{
        UsersServiceMock()
    }

    override val lobbyServices by lazy{
        LobbyServiceImp(repoLobby)
    }
}