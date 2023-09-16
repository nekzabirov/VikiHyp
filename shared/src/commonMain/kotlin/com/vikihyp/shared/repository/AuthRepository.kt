package com.vikihyp.shared.repository

import com.nekzabirov.firebaseauth.KFireAuth

interface AuthRepository {
    /***
     * @param phoneNumber user user input phone number
     * @return verificationID
     * @throws InternetError, Phone is in black list, firebase security
     */
    suspend fun sendVerificationPhone(phoneNumber: String): String

    /**
     * @param verificationID is return from AuthRepository.sendVerificationPhone
     * @param code is sms code user received
     */
    suspend fun verificationAuth(verificationID: String, code: String): Boolean

    suspend fun googleSignIn(accountToken: String): Boolean
}