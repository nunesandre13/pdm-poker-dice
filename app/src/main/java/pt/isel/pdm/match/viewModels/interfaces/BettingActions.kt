package pt.isel.pdm.match.viewModels.interfaces

interface BettingActions {
    suspend fun raiseAnte(ante: Int)
    suspend fun passTurn()
    suspend fun call()
    suspend fun fold()
}