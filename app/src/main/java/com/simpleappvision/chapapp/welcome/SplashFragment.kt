package com.simpleappvision.chapapp.welcome

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.simpleappvision.chapapp.R


class SplashFragment : Fragment() {

    private val mTAG = "SplashFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_splash, container, false)
        delayScreen()
        return view

    }


    private fun delayScreen() {
        object : CountDownTimer(2000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                // Do Nothing ..
            }

            override fun onFinish() {
                checkUserSession()
            }
        }.start()
    }


    private fun checkUserSession() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            // User Has Logged in
            startHomeActivity()
            finishActivity()
            Log.v(mTAG, "User Has Logged in")
            return
        }
        // User Should Log in
        loadSignInFragment()
        Log.v(mTAG, "User Should Log in ")
    }

    private fun startHomeActivity() {
        // TODO : start Home Activity
    }

    private fun finishActivity() {
        activity!!.finish()
    }

    private fun loadSignInFragment() {
        // TODO : LOAD Sign In Fragment
    }


}