package com.example.news.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.news.MainActivity
import com.example.news.datamodels.User
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.CallbackManager.Factory.create
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class FacebookActivity : MainActivity() {
    private lateinit var callbackManager: CallbackManager
    private  val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://socialx-444eb-default-rtdb.firebaseio.com")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel() {
                Toast.makeText(applicationContext, "Canceled", Toast.LENGTH_SHORT).show()
                goBack()
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(applicationContext, "Error occurred", Toast.LENGTH_SHORT).show()
                goBack()

            }
        })
    }

    private fun goBack() {
        onBackPressed()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {


        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    updateUI()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    goBack()
                }
            }
    }

    private fun updateUI() {
        val user = User(auth.currentUser?.displayName.toString(), "FacebookSignIn", auth.currentUser?.email.toString(), auth.currentUser?.uid.toString(), auth.currentUser?.phoneNumber.toString())
        database.reference.child("Users").child("Facebook").child(user.getUserId()).setValue(user)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}