package pt.isel.pdm.match.services

import pt.isel.pdm.domain.Match
import pt.isel.pdm.domain.PlayCommand
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.match.repository.RepositoryMatch

import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success
import pt.isel.pdm.utils.onOutCome

class MatchServiceImp(private val repository: RepositoryMatch) : MatchServices  {

    override suspend fun play(command: PlayCommand): OutCome<Match, MatchError> {
        return repository.play(command).onOutCome(
            onSuccess = { Success(it) },
            onFailure = { Failure(it) }
        )
    }

    override suspend fun leaveMatch(match: Match): OutCome<Match, MatchError> {
        return repository.leaveMatch(match).onOutCome(
            onSuccess = { Success(it) },
            onFailure = { Failure(it) }
        )
    }
}