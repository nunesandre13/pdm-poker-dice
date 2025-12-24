package pt.isel.pdm.configuration

import android.app.Application
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import pt.isel.pdm.httpConfig.KtorNetworkClient
import pt.isel.pdm.httpConfig.NetworkClient

import pt.isel.pdm.lobby.repository.RepositoryLobbies
import pt.isel.pdm.lobby.repository.RepositoryLobbiesMock
import pt.isel.pdm.lobby.services.LobbyServiceImp
import pt.isel.pdm.match.foreGround.MatchLifecycleObserver
import pt.isel.pdm.match.repository.RepositoryMatch
import pt.isel.pdm.match.repository.RepositoryMatchHttp
import pt.isel.pdm.match.repository.RepositoryMatchMock
import pt.isel.pdm.match.services.MatchServiceImp
import pt.isel.pdm.match.services.MatchServices

import pt.isel.pdm.user.services.UsersServiceMock
import pt.isel.pdm.utils.PlayersNameCache
import kotlin.time.Duration.Companion.seconds


class MockConfiguration : Application(), DependenciesContainer {


    init {
        Log.d("Mock", "Mock configuration created")
    }

     override fun onCreate() {
        super.onCreate()
         ProcessLifecycleOwner.get().lifecycle.addObserver(MatchLifecycleObserver(this))
     }


    override val playersNameCache: PlayersNameCache by lazy {
        PlayersNameCache()
    }

    override val repoLobby: RepositoryLobbies by lazy {
        RepositoryLobbiesMock()
    }

    override val userServices by lazy{
        UsersServiceMock()
    }

    override val lobbyServices by lazy{
        LobbyServiceImp(repoLobby, playersNameCache,userServices)
    }

    override val matchRepo: RepositoryMatch by lazy {
        RepositoryMatchMock()
    }

    override val matchService: MatchServices by lazy {
        MatchServiceImp(matchRepo)
    }

}