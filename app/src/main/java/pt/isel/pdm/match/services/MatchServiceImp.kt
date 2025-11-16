package pt.isel.pdm.match.services

import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.Match
import pt.isel.pdm.domain.PlayCommand
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.match.repository.RepositoryMatch
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success
import pt.isel.pdm.utils.onOutCome

class MatchServiceImp(private val repository: RepositoryMatch) : MatchServices {


    override fun getMatchUpdate(matchId: Int): OutCome<Flow<Match>, MatchError> {
        return repository.matchSseListener.
    }

    override suspend fun rollDice(playerId: Int, roundId: Int, dices: List<DiceFace>): OutCome<Unit, MatchError> {
        return repository.play(PlayCommand.RollDice(playerId, roundId, dices)).onOutCome(
            onSuccess = { Success(Unit) },
            onFailure = { Failure(it) }
        )
    }

    override suspend fun setHand(playerId: Int, roundId: Int): OutCome<Unit, MatchError> {
        return repository.play(PlayCommand.SetHand(playerId, roundId)).onOutCome(
            onSuccess = { Success(Unit) },
            onFailure = { Failure(it) }
        )
    }

    override suspend fun raiseAnte(
        playerId: Int,
        roundId: Int,
        ante: Int
    ): OutCome<Unit, MatchError> {
        return repository.play(PlayCommand.RaiseAnte(playerId, roundId, ante)).onOutCome(
            onSuccess = { Success(Unit) },
            onFailure = { Failure(it) }
        )
    }

    override suspend fun passTurn(playerId: Int, roundId: Int): OutCome<Unit, MatchError> {
        return repository.play(PlayCommand.PassTurn(playerId, roundId)).onOutCome(
            onSuccess = { Success(Unit) },
            onFailure = { Failure(it) }
        )
    }

    override suspend fun call(playerId: Int, roundId: Int): OutCome<Unit, MatchError> {
        return repository.play(PlayCommand.Call(playerId, roundId)).onOutCome(
            onSuccess = { Success(Unit) },
            onFailure = { Failure(it) }
        )
    }

    override suspend fun fold(playerId: Int, roundId: Int): OutCome<Unit, MatchError> {
        return repository.play(PlayCommand.Fold(playerId, roundId)).onOutCome(
            onSuccess = { Success(Unit) },
            onFailure = { Failure(it) }
        )
    }


    override suspend fun leaveMatch(match: Match): OutCome<Unit, MatchError> {
        return repository.leaveMatch(match).onOutCome(
            onSuccess = { Success(Unit) },
            onFailure = { Failure(it) }
        )
    }
}
