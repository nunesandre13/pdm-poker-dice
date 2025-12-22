package pt.isel.pdm.domain.state

import pt.isel.pdm.domain.INITIAL_ANTE
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.PlayerId
import pt.isel.pdm.domain.PlayerMatchState
import pt.isel.pdm.domain.PlayerMatchStateWithName
import pt.isel.pdm.domain.PlayerRoundState
import pt.isel.pdm.domain.PlayerStatus
import pt.isel.pdm.domain.RawRound
import pt.isel.pdm.domain.RoundId
import pt.isel.pdm.domain.RoundState
import pt.isel.pdm.domain.withName
import pt.isel.pdm.utils.PlayerNameResolver

data class Round(
    val id: RoundId,
    val players: List<PlayerRoundStateWithName>,
    val ante: Int = INITIAL_ANTE,
    val totalBet: Int =(INITIAL_ANTE),
    val state: RoundState
)

fun RawRound.toRound(playersWithNames: List<PlayerRoundStateWithName>): Round {
    return Round(
        id = this.id,
        players = playersWithNames,
        ante = this.ante,
        totalBet = this.totalBet,
        state = this.state
    )
}

data class PlayerRoundStateWithName(
    val playerId: PlayerId,
    val name: Name,
    val coins: Int,
    val playerStatus: PlayerStatus = PlayerStatus.NotStarted
)

fun PlayerRoundState.withName(name: Name): PlayerRoundStateWithName {
    return PlayerRoundStateWithName(
        playerId = this.playerId,
        name = name,
        coins = this.coins,
        playerStatus = this.playerStatus
    )
}


fun List<PlayerRoundState>.mapping(resolver: PlayerNameResolver): List<PlayerRoundStateWithName> = map { player ->
    player.withName(resolver[player.playerId])
}
