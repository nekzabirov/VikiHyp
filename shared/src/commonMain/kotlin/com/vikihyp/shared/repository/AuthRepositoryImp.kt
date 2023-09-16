package com.vikihyp.shared.repository

import com.nekzabirov.firebaseauth.KFireAuth

class AuthRepositoryImp : AuthRepository {
    private val firebaseAuth = KFireAuth.getInstance()

    override suspend fun sendVerificationPhone(phoneNumber: String): String {
        return firebaseAuth.phone.verifyPhoneNumber(phoneNumber)
    }

    override suspend fun verificationAuth(verificationID: String, code: String): Boolean {
        val credential = firebaseAuth.phone.getCredential(verificationID, code)

        return firebaseAuth.signInWithCredential(credential)
    }

    override suspend fun googleSignIn(accountToken: String): Boolean {
        val credential = firebaseAuth.google.getCredential(accountToken)

        return firebaseAuth.signInWithCredential(credential)
    }
}