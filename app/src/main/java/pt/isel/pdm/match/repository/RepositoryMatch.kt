package pt.isel.pdm.match.repository

import kotlinx.coroutines.flow.SharedFlow
import pt.isel.pdm.domain.RawMatch
import pt.isel.pdm.domain.PlayCommand
import pt.isel.pdm.domain.events.MatchResponse
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.utils.OutCome

interface RepositoryMatch {

    fun matchSseListener(matchId: Int): SharedFlow<OutCome<MatchResponse, MatchError>>
    suspend fun play(command: PlayCommand):OutCome<RawMatch, MatchError>
    suspend fun leaveMatch(match:RawMatch):OutCome<RawMatch, MatchError>

}