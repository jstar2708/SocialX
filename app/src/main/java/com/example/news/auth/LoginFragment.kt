package com.example.news.auth

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.news.MainActivity
import com.example.news.R
import com.example.news.auth.viewmodels.LoginViewModel
import com.example.news.databinding.FragmentLoginBinding
import com.example.news.datamodels.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.GoogleAuthProvider


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            handleResults(task)
        }
        else{
            Toast.makeText(requireContext(), "Request Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account: GoogleSignInAccount? = task.result
            if(account != null){
                updateUI(account)
            }
        }
        else{
            Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        viewModel.auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                addUserToDatabase(account)
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            else{
                Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addUserToDatabase(account: GoogleSignInAccount) {
        val user = User(account.displayName.toString(),"GoogleSignIn", account.email.toString(),viewModel.auth.currentUser?.uid.toString() , "null")
        viewModel.database.reference.child("Users").child("Google").child(viewModel.auth.currentUser?.uid.toString()).setValue(user)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application))[LoginViewModel::class.java]

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Login user")
        progressDialog.setMessage("Wait while we Log you in")
        progressDialog.setCanceledOnTouchOutside(false)

        if(viewModel.isUserSignedIn()){
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        viewModel.action.observe(requireActivity(), Observer {
            if(it == 1){
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            else if(it == 2){
                Toast.makeText(requireContext(), "Error while signing in", Toast.LENGTH_SHORT).show()
            }
        })

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.googleButton.setOnClickListener {
            signInWithGoogle()
        }
        binding.facebookButton.setOnClickListener {
            val intent = Intent(requireActivity(), FacebookActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
            requireActivity().finish()
        }

        binding.forgotPassword.setOnClickListener {
            Toast.makeText(requireContext(), "Forgot Password", Toast.LENGTH_SHORT).show()
        }

        binding.loginButton.setOnClickListener {
            progressDialog.show()
            Handler().postDelayed({
                if(!binding.email.text.isNullOrEmpty() && !binding.password.text.isNullOrEmpty()){
                    viewModel.signInWithEmail(binding.email.text.toString(), binding.password.text.toString())
                    progressDialog.dismiss()
                }
            }, 2000)
            progressDialog.dismiss()
        }

        binding.registerNow.setOnClickListener {
            val tabHost = requireActivity().findViewById<TabLayout>(R.id.tabLayout)
            tabHost.getTabAt(1)?.select()
        }

        return binding.root
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

}