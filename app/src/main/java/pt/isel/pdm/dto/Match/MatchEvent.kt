package pt.isel.pdm.dto.Match

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.events.MatchResponse

@Serializable
sealed interface MatchEvent {

    @Serializable
    @SerialName("NEW_MATCH")
    data class NewMatchOutput(val match: MatchIn) : MatchEvent

    @Serializable
    @SerialName("MATCH_ENDED")
    data object MatchEndedOutput : MatchEvent

    fun toDomain(): MatchResponse = when(this) {
        is NewMatchOutput -> MatchResponse.NewMatch(match.toDomain())
        MatchEndedOutput -> MatchResponse.MatchEnded
    }
}
