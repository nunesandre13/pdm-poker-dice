package pt.isel.pdm.domain

import pt.isel.pdm.domain.state.Round
import pt.isel.pdm.utils.PlayerNameResolver

data class Match(
    val id: MatchId,
    val players: List<PlayerMatchStateWithName>,
    val owner: UserId,
    val actualRound : Round,
    val initialCoins: Int,
    val remainingRounds: Int,
    val matchStatus: MatchStatus
)

fun RawMatch.toMatch(playersWithNames: List<PlayerMatchStateWithName>, round: Round): Match {
    return Match(
        id = this.id,
        players = playersWithNames,
        owner = this.owner,
        actualRound = round,
        initialCoins = this.initialCoins,
        remainingRounds = this.remainingRounds,
        matchStatus = this.matchStatus
    )
}


class PlayerMatchStateWithName(val playerId: PlayerId,val name: Name, val coins: Int)

fun PlayerMatchState.withName(name: Name) = PlayerMatchStateWithName(playerId,name,coins)