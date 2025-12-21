package pt.isel.pdm.domain

@JvmInline
value class PlayerId(override val id : Int): Id

@JvmInline
value class LobbyId(override val id : Int): Id

@JvmInline
value class UserId(override val id : Int): Id

@JvmInline
value class MatchId(override val id : Int): Id

@JvmInline
value class RoundId(override val id : Int): Id


interface Id : Comparable<Id> {
    val id : Int
    override fun compareTo(other: Id): Int = id.compareTo(other.id)
}