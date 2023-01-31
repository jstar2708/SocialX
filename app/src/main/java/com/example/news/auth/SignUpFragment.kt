package com.example.news.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.news.MainActivity
import com.example.news.R
import com.example.news.auth.viewmodels.SignUpViewModel
import com.example.news.databinding.FragmentSignUpBinding
import com.example.news.datamodels.User
import com.google.android.material.tabs.TabLayout

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application))[SignUpViewModel::class.java]

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Register user")
        progressDialog.setMessage("Wait while we register you")
        progressDialog.setCanceledOnTouchOutside(false)

        viewModel.action.observe(requireActivity(), Observer {
            if(it == 1){
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            else if(it == 2){
                Toast.makeText(requireContext(), "Error while signing in!", Toast.LENGTH_SHORT).show()
            }
        })

        binding.signIn.setOnClickListener {
            val tabHost = requireActivity().findViewById<TabLayout>(R.id.tabLayout)
                tabHost.getTabAt(0)?.select()
        }

        binding.registerButton.setOnClickListener {
            progressDialog.show()
            Handler().postDelayed({
                if(!binding.name.text.isNullOrEmpty() && !binding.email.text.isNullOrEmpty()
                    && !binding.password.text.isNullOrEmpty() && binding.phoneNumber.text.length == 10
                    && binding.checkBox.isChecked){

                    val user = User()
                    user.setUserName(binding.name.text.toString())
                    user.setEmail(binding.email.text.toString())
                    user.setPassword(binding.password.text.toString())
                    user.setPhoneNumber("+${binding.ccp.selectedCountryCode}${binding.phoneNumber.text}")
                    viewModel.registerUser(user)
                    progressDialog.dismiss()
                }
            }, 2000)
            progressDialog.dismiss()
        }

        return binding.root
    }

}