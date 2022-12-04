package kr.dagger.domain.usecase.auth

import kotlinx.coroutines.flow.Flow
import kr.dagger.domain.model.Response
import kr.dagger.domain.repository.AuthRepository

class SignInGoogleUseCase(
	private val repository: AuthRepository
) {
	suspend operator fun invoke(idToken: String) : Flow<Response<Unit>> {
		return repository.signInGoogle(idToken)
	}
}