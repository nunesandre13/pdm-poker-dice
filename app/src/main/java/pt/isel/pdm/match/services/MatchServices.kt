package pt.isel.pdm.match.services


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.RawMatch
import pt.isel.pdm.domain.MatchId
import pt.isel.pdm.domain.PlayerId
import pt.isel.pdm.domain.RoundId
import pt.isel.pdm.domain.events.MatchResponse
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.utils.OutCome

interface MatchServices {

    fun getMatchUpdate(matchId: MatchId): Flow<OutCome<MatchResponse, MatchError>>
    suspend fun rollDice(playerId: PlayerId, roundId: RoundId, dices: List<DiceFace>): OutCome<Unit, MatchError>
    suspend fun setHand(playerId: PlayerId, roundId: RoundId) : OutCome<Unit, MatchError>
    suspend fun raiseAnte(playerId: PlayerId, roundId: RoundId,ante: Int) : OutCome<Unit, MatchError>
    suspend fun passTurn(playerId: PlayerId, roundId: RoundId) : OutCome<Unit, MatchError>
    suspend fun call(playerId: PlayerId, roundId: RoundId,) : OutCome<Unit, MatchError>
    suspend fun fold(playerId: PlayerId, roundId: RoundId,) : OutCome<Unit, MatchError>
    suspend fun leaveMatch(match: RawMatch):  OutCome<Unit, MatchError>
    val matchIdState: StateFlow<Int?>
}