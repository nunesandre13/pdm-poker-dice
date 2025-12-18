package pt.isel.pdm.match.services


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.Match
import pt.isel.pdm.domain.events.MatchResponse
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.utils.OutCome

interface MatchServices {

    fun getMatchUpdate(matchId: Int): Flow<OutCome<MatchResponse, MatchError>>
    suspend fun rollDice(playerId: Int, roundId: Int, dices: List<DiceFace>): OutCome<Unit, MatchError>
    suspend fun setHand(playerId: Int, roundId: Int) : OutCome<Unit, MatchError>
    suspend fun raiseAnte(playerId: Int, roundId: Int,ante: Int) : OutCome<Unit, MatchError>
    suspend fun passTurn(playerId: Int, roundId: Int) : OutCome<Unit, MatchError>
    suspend fun call(playerId: Int, roundId: Int,) : OutCome<Unit, MatchError>
    suspend fun fold(playerId: Int, roundId: Int,) : OutCome<Unit, MatchError>
    suspend fun leaveMatch(match: Match):  OutCome<Unit, MatchError>
    val matchIdState: StateFlow<Int?>
}