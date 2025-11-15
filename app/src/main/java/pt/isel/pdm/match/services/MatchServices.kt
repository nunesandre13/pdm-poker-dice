package pt.isel.pdm.match.services

import pt.isel.pdm.domain.Match
import pt.isel.pdm.domain.PlayCommand
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.utils.OutCome

interface MatchServices {

    suspend fun play(command: PlayCommand): OutCome<Match, MatchError>

    suspend fun leaveMatch(match: Match): OutCome<Match, MatchError>
}