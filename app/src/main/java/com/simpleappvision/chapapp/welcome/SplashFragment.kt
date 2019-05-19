package com.simpleappvision.chapapp.welcome

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.simpleappvision.chapapp.R
import com.simpleappvision.chapapp.authentication.SigninFragment
import com.simpleappvision.chapapp.home.HomeActivity


class SplashFragment : Fragment() {

    private val mTAG = "SplashFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_splash, container, false)
        delayScreen()
        return view

    }

    // For Showing App Logo 2 Seconds
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

    // Check Has User Signed In?
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

    // Change Screen Area
    private fun startHomeActivity() {
        // TODO : start Home Activity
        var intent = Intent(activity!!,HomeActivity::class.java)
        startActivity(intent)
    }
    private fun loadSignInFragment() {
        val transaction: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.layoutAuthenticationFragmentPlace, SigninFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun finishActivity() {
        activity!!.finish()
    }

}