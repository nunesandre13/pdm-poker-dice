package pt.isel.pdm.match.viewModels.interfaces

interface BettingActions {
    suspend fun raiseAnte(ante: Int): Boolean
    suspend fun passTurn() : Boolean
    suspend fun call() : Boolean
    suspend fun fold() : Boolean
}