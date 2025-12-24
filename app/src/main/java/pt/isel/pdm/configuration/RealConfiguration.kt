package pt.isel.pdm.configuration

import android.app.Application
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import pt.isel.pdm.httpConfig.KtorNetworkClient
import pt.isel.pdm.httpConfig.NetworkClient
import pt.isel.pdm.lobby.repository.RepositoryLobbies
import pt.isel.pdm.lobby.repository.RepositoryLobbiesHttp
import pt.isel.pdm.lobby.services.LobbyServiceImp
import pt.isel.pdm.match.foreGround.MatchLifecycleObserver
import pt.isel.pdm.match.repository.RepositoryMatch
import pt.isel.pdm.match.repository.RepositoryMatchHttp
import pt.isel.pdm.match.services.MatchServiceImp
import pt.isel.pdm.match.services.MatchServices
import pt.isel.pdm.user.UserPreferences
import pt.isel.pdm.user.services.UserServicesHttp
import pt.isel.pdm.utils.PlayersNameCache
import kotlin.time.Duration.Companion.seconds


class RealConfiguration : Application(), DependenciesContainer {


    init {
        Log.d("REALLL", "REALLL configuration created")
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(MatchLifecycleObserver(this))
    }

    private val networkClient: NetworkClient by lazy {
        KtorNetworkClient(3.seconds)
    }

    override val playersNameCache: PlayersNameCache by lazy {
        PlayersNameCache()
    }
    override val repoLobby: RepositoryLobbies by lazy {
        RepositoryLobbiesHttp(userPreferences, networkClient)
    }

    private val userPreferences by lazy {
        UserPreferences(this)
    }

    override val userServices by lazy{
        UserServicesHttp(networkClient,userPreferences)
    }

    override val lobbyServices by lazy{
        LobbyServiceImp(repoLobby, playersNameCache,userServices)
    }

    override val matchRepo: RepositoryMatch by lazy {
        RepositoryMatchHttp(networkClient,userPreferences)
    }

    override val matchService: MatchServices by lazy {
        MatchServiceImp(matchRepo)
    }

}