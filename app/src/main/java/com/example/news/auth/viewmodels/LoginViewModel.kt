package com.example.news.auth.viewmodels

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginViewModel: ViewModel() {
    val action: MutableLiveData<Int> = MutableLiveData()
    fun isUserSignedIn():Boolean {
        return auth.currentUser != null
    }

    fun signInWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                action.value = 1
            }
            else{
                action.value = 2
            }
        }
    }

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://socialx-444eb-default-rtdb.firebaseio.com")


}