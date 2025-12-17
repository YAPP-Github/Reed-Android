package com.ninecraft.booket.feature.login.client

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.ninecraft.booket.core.designsystem.R
import com.orhanobut.logger.Logger
import dev.zacsweers.metro.Inject

@Inject
internal class GoogleLoginClient {
    suspend fun loginWithGoogle(
        context: Context,
        webClientId: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit,
    ) {
        val credentialManager = CredentialManager.create(context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(true)
            .build()

        val credentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                request = credentialRequest,
                context = context,
            )
            handleSignIn(result, onSuccess, onFailure, context)
        } catch (e: GetCredentialCancellationException) {
            Logger.e("Google 로그인 취소됨, ${e.message}")
            onFailure(context.getString(R.string.unknown_error_message))
        } catch (e: NoCredentialException) {
            Logger.e("Google 계정을 찾을 수 없음, ${e.message}")
            onFailure(context.getString(R.string.unknown_error_message))
        } catch (e: GetCredentialException) {
            Logger.e("Google 로그인 실패, ${e.message}")
            onFailure(context.getString(R.string.unknown_error_message))
        } catch (e: Exception) {
            Logger.e("알 수 없는 오류: ${e.message}")
            onFailure(context.getString(R.string.unknown_error_message))
        }
    }

    private fun handleSignIn(
        result: GetCredentialResponse,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit,
        context: Context,
    ) {
        val credential = result.credential

        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken
                Logger.d("Google 로그인 성공: ${googleIdTokenCredential.id}")
                onSuccess(idToken)
            } catch (e: Exception) {
                Logger.e("Google ID Token 파싱 실패: ${e.message}")
                onFailure(context.getString(R.string.unknown_error_message))
            }
        } else {
            Logger.e("예상치 못한 credential type: ${credential.type}")
            onFailure(context.getString(R.string.unknown_error_message))
        }
    }
}
