package com.example.tracker.models.auth

interface Auth {

    suspend fun signIn(userEmail: String, userPassword: String)

    suspend fun signUp(userEmail: String, userPassword: String)

    suspend fun forgotPassword(userEmail: String)

    fun signOut()

    fun isSignedIn(): Boolean

    fun getCurrentUserId(): String

}
