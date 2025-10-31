package com.example.obligatoriskopgave.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    var user = mutableStateOf(auth.currentUser)
        private set
    var errorMessage = mutableStateOf("")
        private set
    var isDialogShown by mutableStateOf(false)
        private set


    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                user.value = auth.currentUser
                errorMessage.value = "Sign in successful"
                isDialogShown = false //close dialog on succes
            } else {
                errorMessage.value = task.exception?.message ?: "Sign in failed"
            }
        }
    }

    fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                user.value = auth.currentUser
                errorMessage.value = "Sign up successful"
                isDialogShown = false //close dialog on succes
            } else {
                errorMessage.value = task.exception?.message ?: "Sign up failed"
            }
        }
    }

    fun signOut() {
        auth.signOut()
        user.value = null
    }


    fun onLogin() {
        isDialogShown = true

    }

    fun onDismissDialog() {
        isDialogShown = false

    }
}