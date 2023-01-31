package com.example.news.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.news.auth.adapter.ViewPagerAdapter
import com.example.news.databinding.ActivityAuthenticationBinding
import com.google.android.material.tabs.TabLayoutMediator

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(binding.tabLayout, binding.viewPager, TabLayoutMediator
            .TabConfigurationStrategy { tab, position ->
                if(position == 0){
                    tab.text = "Login"
                }
                else{
                    tab.text = "Sign Up"
                }
            }).attach()
    }

    fun swipe(id: Int){
        binding.viewPager.setCurrentItem(id, true)
    }
}