package pt.isel.pdm.dto.Match

import pt.isel.pdm.domain.events.MatchResponse

sealed interface MatchEvent {
    class NewMatchOutput(val match : MatchIn) : MatchEvent
    data object MatchEndedOutput : MatchEvent
}

fun MatchEvent.toDomain(): MatchResponse = TODO()