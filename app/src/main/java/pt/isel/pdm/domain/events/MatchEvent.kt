package pt.isel.pdm.domain.events

import pt.isel.pdm.domain.match.RawMatch

sealed class MatchResponse {
    class NewMatch(val newMatch: RawMatch) : MatchResponse()
    data object MatchEnded: MatchResponse()
}