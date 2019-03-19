package com.simpleappvision.chapapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.simpleappvision.chapapp.welcome.SplashFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadSplashFragment()
    }


    private fun loadSplashFragment(){
        val transaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.layoutAuthenticationFragmentPlace,SplashFragment())
        transaction.addToBackStack(null)
        transaction.commit()

    }



}
