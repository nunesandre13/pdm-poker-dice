package pt.isel.pdm.configuration

import android.app.Application
import android.util.Log
import pt.isel.pdm.lobby.services.LobbyServiceMock
import pt.isel.pdm.user.services.UsersServiceMock


// por agora pode ser assim, futuramente deve se utilizar uma class que herda de application()
class MockConfiguration : Application(), DependenciesContainer {
    init {
        Log.d("Mock", "Mock configuration created")
    }
    override val userServices by lazy{
        UsersServiceMock()
    }
    override val lobbyServices by lazy{
        LobbyServiceMock()
    }
}