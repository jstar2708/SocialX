package com.example.news.auth.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.datamodels.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpViewModel: ViewModel() {
    val action: MutableLiveData<Int> = MutableLiveData()
    fun registerUser(user: User) {
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener {
            if(it.isSuccessful){
                user.setUserId(auth.currentUser?.uid.toString())
                database.reference.child("Users").child("Custom").child(auth.currentUser?.uid.toString()).setValue(user).addOnCompleteListener {
                    if(it.isSuccessful){
                        action.value = 1
                    }
                    else{
                        action.value = 2
                        auth.currentUser?.delete()
                    }
                }
            }
        }
    }

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://socialx-444eb-default-rtdb.firebaseio.com")
}