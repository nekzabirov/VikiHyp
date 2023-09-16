package com.vikihyp.shared.screen

import com.vikihyp.shared.repository.AuthRepository
import com.vikihyp.shared.ui.ViewEvent
import com.vikihyp.shared.ui.ViewModel
import com.vikihyp.shared.ui.ViewState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.inject

internal sealed interface AuthState : ViewState {
    data object None : AuthState
    data object AuthSuccess : AuthState

    data class PhoneVerification(
        val verificationID: String,
        val isLoading: Boolean,
        val isFail: Boolean
    ) : AuthState

    data class GoogleSignIn(val isLoading: Boolean, val isError: Boolean) : AuthState
}

internal sealed interface AuthViewEvent : ViewEvent {
    data class SendPhoneVerification(val phoneNumber: String) : AuthViewEvent
    data class PhoneSignIn(val verificationID: String, val verificationCode: String) : AuthViewEvent
    data class GoogleSignIn(val accountToken: String) : AuthViewEvent
    data class FacebookSignIn(val accountToken: String) : AuthViewEvent
}

internal class AuthViewModel : ViewModel<AuthState, AuthViewEvent>() {
    private val authRepository: AuthRepository by inject()

    override fun internalState(): AuthState = AuthState.None

    override fun processEvent(event: AuthViewEvent) {
        when (event) {
            is AuthViewEvent.SendPhoneVerification -> sendPhoneVerification(event.phoneNumber)
            is AuthViewEvent.PhoneSignIn -> signIn(verificationID = event.verificationID, verificationCode = event.verificationCode)
            is AuthViewEvent.GoogleSignIn -> googleSignIn(event.accountToken)
            is AuthViewEvent.FacebookSignIn -> TODO()
        }
    }

    override fun processError(error: Throwable) {
        TODO("Not yet implemented")
    }

    private fun sendPhoneVerification(phoneNumber: String) = launch {
        setState(AuthState.PhoneVerification(verificationID = "", isLoading = true, isFail = false))

        val verificationID = try {
            authRepository.sendVerificationPhone(phoneNumber)
        } catch (e: Exception) {
            e.printStackTrace()
            setState(AuthState.PhoneVerification(verificationID = "", isLoading = false, isFail = true))
            return@launch
        }

        setState(AuthState.PhoneVerification(verificationID = verificationID, isLoading = false, isFail = false))
    }

    private fun signIn(verificationID: String, verificationCode: String) = launch {
        setState(AuthState.PhoneVerification(verificationID = verificationID, isLoading = true, isFail = false))

        val success = try {
            authRepository.verificationAuth(verificationID = verificationID, code = verificationCode)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

        if (success)
            setState(AuthState.AuthSuccess)
        else
            setState(AuthState.PhoneVerification(verificationID = verificationID, isLoading = false, isFail = true))
    }

    private fun googleSignIn(accountToken: String) = launch {
        setState(AuthState.GoogleSignIn(isLoading = true, isError = false))

        val result = authRepository.googleSignIn(accountToken = accountToken)

        if (result)
            setState(AuthState.AuthSuccess)
        else
            setState(AuthState.GoogleSignIn(isLoading = false, isError = true))
    }
}