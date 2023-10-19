package com.example.tracker.models_impl.auth

import com.example.tracker.models.auth.Auth
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class FireBaseAuth : Auth {

    private val auth = FirebaseAuth.getInstance()

    override suspend fun signIn(userEmail: String, userPassword: String) {
        auth.signInWithEmailAndPassword(userEmail, userPassword).await()
    }

    override suspend fun signUp(userEmail: String, userPassword: String) {
        auth.createUserWithEmailAndPassword(userEmail, userPassword).await()
    }

    override suspend fun forgotPassword(userEmail: String) {
        auth.sendPasswordResetEmail(userEmail).await()
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun isSignedIn(): Boolean = auth.currentUser != null

    override fun getCurrentUserId(): String = auth.currentUser?.uid.toString()

}
