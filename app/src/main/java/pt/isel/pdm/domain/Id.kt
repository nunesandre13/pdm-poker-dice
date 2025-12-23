package pt.isel.pdm.domain

@JvmInline
value class PlayerId(override val id : Int): Id<PlayerId>

@JvmInline
value class LobbyId(override val id : Int): Id<LobbyId>

@JvmInline
value class UserId(override val id : Int): Id<UserId>

@JvmInline
value class MatchId(override val id : Int): Id<MatchId>

@JvmInline
value class RoundId(override val id : Int): Id<RoundId>


interface Id<T : Id<T>> : Comparable<T> {
    val id: Int
    override fun compareTo(other: T): Int = id.compareTo(other.id)
}
