package com.simpleappvision.chapapp.authentication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.simpleappvision.chapapp.R
import com.simpleappvision.chapapp.home.HomeActivity
import com.simpleappvision.chapapp.model.User
import kotlinx.android.synthetic.main.fragment_signin.view.*

class SigninFragment : Fragment(), View.OnClickListener {


    // Variable For Log
    private val mTAG = "SignInFragment"

    // Fragment View For Using In Functions
    private lateinit var viewFragment: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewFragment = inflater.inflate(R.layout.fragment_signin, container, false)

        // Add EditText Listener For Handling EditText
        viewFragment.editTextSigninEmail.addTextChangedListener(watcher)
        viewFragment.editTextSigninPassword.addTextChangedListener(watcher)

        // Add Buttons Click Listener
        viewFragment.textViewForgotPasswordLink.setOnClickListener(this)
        viewFragment.textViewSignupLink.setOnClickListener(this)
        viewFragment.buttonSignin.setOnClickListener(this)

        return viewFragment
    }


    // Handle EditText's texts
    private var watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Do Nothing..
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if (isEmailValid() && isPasswordValid()) {
                setEnableSignInButton()
            } else {
                setDisableSignInButton()
            }
        }

        override fun afterTextChanged(s: Editable?) {
            // Do Nothing ...
        }
    }

    // Handle Clicks
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.buttonSignin -> {
                showProgressBar()
                disableScreen()
                signInWithEmailAndPassword()
            }
            R.id.textViewForgotPasswordLink -> {
                loadResetPasswordFragment()
            }
            R.id.textViewSignupLink -> {
                loadSignUpFragment()
            }
        }
    }

    // Check Email And Password
    private fun isEmailValid(): Boolean {
        if (viewFragment.editTextSigninEmail.text.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(viewFragment.editTextSigninEmail.text).matches()) {
            return true
        }
        return false
    }

    private fun isPasswordValid(): Boolean {
        if (viewFragment.editTextSigninPassword.text.length > 5) {
            return true
        }
        return false
    }

    //
    private fun setEnableSignInButton() {
        viewFragment.buttonSignin.isEnabled = true
        viewFragment.buttonSignin.setTextColor(ContextCompat.getColor(context!!, R.color.colorAccent))
        viewFragment.buttonSignin.alpha = 1F
    }

    private fun setDisableSignInButton() {
        viewFragment.buttonSignin.isEnabled = false
        viewFragment.buttonSignin.setTextColor(ContextCompat.getColor(context!!, R.color.colorText))
        viewFragment.buttonSignin.alpha = 0.3F
    }

    // Sign In Area
    private fun signInWithEmailAndPassword() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            viewFragment.editTextSigninEmail.text.toString(),
            viewFragment.editTextSigninPassword.text.toString()
        ).addOnCompleteListener { p0 ->
            if (p0.isSuccessful) {
                // user has signed in
                Log.v(mTAG, "User Has Singed in")
                startHomeActivity()
                finishActivity()
            } else {
                // authentication failed
                hideProgressBar()
                enableScreen()
                Toast.makeText(activity, getString(R.string.error_authentication_failed), Toast.LENGTH_LONG).show()
                Log.e(mTAG, "Authentication Failed")
            }
        }
    }


    // Progress Bar Area
    private fun showProgressBar() {
        viewFragment.groupProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        viewFragment.groupProgressBar.visibility = View.GONE
    }

    // Other Objects Must Be Disable During Progress Bar is Showing
    private fun disableScreen() {
        viewFragment.editTextSigninEmail.isEnabled = false
        viewFragment.editTextSigninPassword.isEnabled = false
        viewFragment.buttonSignin.isEnabled = false
        viewFragment.textViewSignupLink.isEnabled = false
        viewFragment.textViewForgotPasswordLink.isEnabled = false
    }

    private fun enableScreen() {
        viewFragment.editTextSigninEmail.isEnabled = true
        viewFragment.editTextSigninPassword.isEnabled = true
        viewFragment.buttonSignin.isEnabled = true
        viewFragment.textViewSignupLink.isEnabled = true
        viewFragment.textViewForgotPasswordLink.isEnabled = true
    }

    // Change Screen Area
    private fun startHomeActivity() {
        startActivity(Intent(activity!!,HomeActivity::class.java))
    }

    private fun finishActivity() {
        activity!!.finish()
    }


    private fun loadSignUpFragment() {
        val transaction: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.layoutAuthenticationFragmentPlace, FullnameFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun loadResetPasswordFragment() {
        val transaction: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.layoutAuthenticationFragmentPlace, ResetPasswordFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

}