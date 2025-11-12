package pt.isel.pdm.domain.events

import pt.isel.pdm.domain.Match

sealed class MatchResponse {
    class NewMatch(val newMatch: Match) : MatchResponse()
    data object MatchEnded: MatchResponse()
}