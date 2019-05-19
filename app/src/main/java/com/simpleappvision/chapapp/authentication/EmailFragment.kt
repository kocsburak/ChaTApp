package com.simpleappvision.chapapp.authentication

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
import com.simpleappvision.chapapp.R
import com.simpleappvision.chapapp.data.SharedPreferencesHelper
import kotlinx.android.synthetic.main.fragment_signup_email.view.*


class EmailFragment : Fragment(), View.OnClickListener, TextWatcher {

    private var mTAG = "EmailFragment"
    private lateinit var viewFragment: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewFragment = inflater.inflate(R.layout.fragment_signup_email, container, false)
        viewFragment.editTextEmail.addTextChangedListener(this)
        viewFragment.buttonNext.setOnClickListener(this)
        return viewFragment
    }


    override fun onClick(v: View?) {
        disableScreen()
        showProgressBar()
        isEmailAlreadyUsed()
    }

    override fun afterTextChanged(s: Editable?) {
        // Do Nothing..
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Do Nothing..
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (isEmailValid()) {
            setEnableNextButton()
        } else {
            setDisableNextButton()
        }

    }

    private fun isEmailValid(): Boolean {
        if (viewFragment.editTextEmail.text.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(viewFragment.editTextEmail.text).matches()) {
            return true
        }
        return false
    }

    private fun setEnableNextButton() {
        viewFragment.buttonNext.isEnabled = true
        viewFragment.buttonNext.setTextColor(ContextCompat.getColor(context!!, R.color.colorAccent))
        viewFragment.buttonNext.alpha = 1F
    }

    private fun setDisableNextButton() {
        viewFragment.buttonNext.isEnabled = false
        viewFragment.buttonNext.setTextColor(ContextCompat.getColor(context!!, R.color.colorText))
        viewFragment.buttonNext.alpha = 0.3F
    }

    //This function transmits email to OnComplete Listener for Check Email is valid for database
    private fun isEmailAlreadyUsed() {

        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(viewFragment.editTextEmail.text.toString().trim())
            .addOnCompleteListener { p0 ->
                if (p0.result!!.signInMethods.isNullOrEmpty()) {
                    saveEmail()
                    saveDefaultPhotoUrl()
                    loadPasswordFragment()
                    Log.v(mTAG, "Email is valid ")
                } else {
                    hideProgressBar()
                    enableScreen()
                    Toast.makeText(activity!!, R.string.error_email_has_been_used, Toast.LENGTH_LONG).show()
                    Log.e(mTAG, "Email has already been used")
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
        viewFragment.editTextEmail.isEnabled = false
        viewFragment.buttonNext.isEnabled = false

    }

    private fun enableScreen() {
        viewFragment.editTextEmail.isEnabled = true
        viewFragment.buttonNext.isEnabled = true
    }

    private fun saveEmail(){
        var helper = SharedPreferencesHelper(activity!!)
        helper.updateEmail(viewFragment.editTextEmail.text.toString())
    }

    private fun saveDefaultPhotoUrl(){
        var helper = SharedPreferencesHelper(activity!!)
        helper.updatePhotoUrl("")
    }

    private fun loadPasswordFragment() {
        val transaction: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.layoutAuthenticationFragmentPlace, PasswordFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }


}