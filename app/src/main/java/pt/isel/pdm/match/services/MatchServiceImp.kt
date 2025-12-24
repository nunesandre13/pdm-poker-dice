package pt.isel.pdm.match.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.match.DiceFace
import pt.isel.pdm.domain.match.RawMatch
import pt.isel.pdm.domain.MatchId
import pt.isel.pdm.domain.match.PlayCommand
import pt.isel.pdm.domain.PlayerId
import pt.isel.pdm.domain.RoundId
import pt.isel.pdm.domain.events.MatchResponse
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.match.repository.RepositoryMatch
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success
import pt.isel.pdm.utils.onOutCome

class MatchServiceImp(private val repository: RepositoryMatch) : MatchServices {

    private val _matchIdState: MutableStateFlow<Int?> = MutableStateFlow(null)
    override val matchIdState: StateFlow<Int?> = _matchIdState

    override fun getMatchUpdate(matchId: MatchId): Flow<OutCome<MatchResponse, MatchError>> {
        _matchIdState.value = matchId.id
        return repository.matchSseListener(matchId.id)
    }

    override suspend fun rollDice(playerId: PlayerId, roundId: RoundId,dices: List<DiceFace>): OutCome<Unit, MatchError> {
        return repository.play(PlayCommand.RollDice(playerId, roundId, dices)).onOutCome(
            onSuccess = { Success(Unit) },
            onFailure = { Failure(it) }
        )
    }

    override suspend fun setHand(playerId: PlayerId, roundId: RoundId): OutCome<Unit, MatchError> {
        return repository.play(PlayCommand.SetHand(playerId, roundId)).onOutCome(
            onSuccess = { Success(Unit) },
            onFailure = { Failure(it) }
        )
    }

    override suspend fun raiseAnte(
        playerId: PlayerId,
        roundId: RoundId,
        ante: Int
    ): OutCome<Unit, MatchError> {
        return repository.play(PlayCommand.RaiseAnte(playerId, roundId, ante)).onOutCome(
            onSuccess = { Success(Unit) },
            onFailure = { Failure(it) }
        )
    }

    override suspend fun passTurn(playerId: PlayerId, roundId: RoundId): OutCome<Unit, MatchError> {
        return repository.play(PlayCommand.PassTurn(playerId, roundId)).onOutCome(
            onSuccess = { Success(Unit) },
            onFailure = { Failure(it) }
        )
    }

    override suspend fun call(playerId: PlayerId, roundId: RoundId): OutCome<Unit, MatchError> {
        return repository.play(PlayCommand.Call(playerId, roundId)).onOutCome(
            onSuccess = { Success(Unit) },
            onFailure = { Failure(it) }
        )
    }

    override suspend fun fold(playerId: PlayerId, roundId: RoundId): OutCome<Unit, MatchError> {
        return repository.play(PlayCommand.Fold(playerId, roundId)).onOutCome(
            onSuccess = { Success(Unit) },
            onFailure = { Failure(it) }
        )
    }


    override suspend fun leaveMatch(match: RawMatch): OutCome<Unit, MatchError> {
        return repository.leaveMatch(match).onOutCome(
            onSuccess = { Success(Unit) },
            onFailure = { Failure(it) }
        )
    }
}
