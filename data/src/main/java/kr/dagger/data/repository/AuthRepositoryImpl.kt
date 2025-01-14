package kr.dagger.data.repository

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kr.dagger.domain.model.Response
import kr.dagger.domain.model.UserInfo
import kr.dagger.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
	private val googleSignInClient: GoogleSignInClient,
	private val auth: FirebaseAuth
) : AuthRepository {

	override fun isLoggedInUser(): Boolean {
		return auth.currentUser != null
	}

	override suspend fun createUser(email: String, password: String, displayName: String): Flow<Response<UserInfo>> = callbackFlow {
		trySend(Response.Loading)
		auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
			if (it.isSuccessful) {
				trySend(
					Response.Success(
						UserInfo(
							id = it.result.user?.uid ?: "",
							givenName = displayName,
							displayName = displayName,
							status = "Newbie",
							profileImageUrl = ""
						)
					)
				)
			} else {
				trySend(Response.Error(it.exception?.message ?: ""))
			}
		}
		awaitClose { cancel() }
	}

	override suspend fun signInEmailAndPassword(email: String, password: String): Flow<Response<Unit>> = callbackFlow {
		trySend(Response.Loading)
		auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
			if (it.isSuccessful) {
				trySend(
					Response.Success(Unit)
				)
			} else {
				trySend(Response.Error(it.exception?.message ?: ""))
			}
		}
		awaitClose { cancel() }
	}

	override suspend fun signInGoogle(idToken: String): Flow<Response<UserInfo>> = callbackFlow {
		trySend(Response.Loading)
		val credential = GoogleAuthProvider.getCredential(idToken, null)
		auth.signInWithCredential(credential)
			.addOnCompleteListener { task ->
				if (task.isSuccessful) {
					trySend(
						Response.Success(
							UserInfo(
								id = task.result.user?.uid ?: "",
								givenName = task.result.user?.displayName ?: "",
								displayName = task.result?.user?.displayName ?: "",
								status = "Newbie",
								profileImageUrl = task.result?.user?.photoUrl.toString()
							)
						)
					)
				} else {
					trySend(Response.Error(task.exception?.message ?: ""))
				}
			}
		awaitClose { cancel() }
	}

	override suspend fun logoutUser(): Flow<Response<Unit>> = flow {
		try {
			emit(Response.Loading)
			googleSignInClient.signOut().await().run {
				emit(Response.Success(Unit))
			}
			auth.signOut()
		} catch (e: Exception) {
			emit(Response.Error(e.message ?: "요청에 실패하였습니다."))
		}
	}

}