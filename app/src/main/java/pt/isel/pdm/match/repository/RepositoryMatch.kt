package pt.isel.pdm.match.repository

import kotlinx.coroutines.flow.SharedFlow
import pt.isel.pdm.domain.Match
import pt.isel.pdm.domain.PlayCommand
import pt.isel.pdm.domain.events.MatchResponse
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.utils.OutCome

interface RepositoryMatch {

    fun matchSseListener(matchId: Int): OutCome<SharedFlow<MatchResponse>, MatchError>
    suspend fun play(command: PlayCommand):OutCome<Match, MatchError>
    suspend fun leaveMatch(match:Match):OutCome<Match, MatchError>

}