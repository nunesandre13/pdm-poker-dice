package pt.isel.pdm.dto.Match

import pt.isel.pdm.domain.events.MatchResponse

sealed interface MatchEvent {
    class NewMatchOutput(val match : MatchIn) : MatchEvent
    data object MatchEndedOutput : MatchEvent

    fun toDomain(): MatchResponse = when(this) {
        is NewMatchOutput -> MatchResponse.NewMatch(match.toDomain())
        MatchEndedOutput -> MatchResponse.MatchEnded
    }
}

