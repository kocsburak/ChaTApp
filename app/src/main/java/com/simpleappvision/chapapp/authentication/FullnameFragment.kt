package com.simpleappvision.chapapp.authentication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.simpleappvision.chapapp.R
import com.simpleappvision.chapapp.data.SharedPreferencesHelper
import kotlinx.android.synthetic.main.fragment_signup_fullname.view.*

class FullnameFragment : Fragment(), View.OnClickListener, TextWatcher {

    private lateinit var viewFragment: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewFragment = inflater.inflate(R.layout.fragment_signup_fullname, container, false)
        viewFragment.editTextFullname.addTextChangedListener(this)
        viewFragment.buttonNext.setOnClickListener(this)
        viewFragment.textViewSigninLink.setOnClickListener(this)

        return viewFragment
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.buttonNext -> {
                saveFullname()
                loadEmailFragment()
            }
            R.id.textViewSigninLink -> {
                loadSignInFragment()
            }
        }
    }


    // EditText Handler
    override fun afterTextChanged(s: Editable?) {
        // Do Nothing ..
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Do Nothing ..
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (isFullnameValid()) {
            setEnableNextButton()
        } else {
            setDisableNextButton()
        }
    }


    private fun isFullnameValid(): Boolean {
        if (viewFragment.editTextFullname.text.isNotEmpty()) {
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


    private fun saveFullname(){
        var helper = SharedPreferencesHelper(activity!!)
        helper.updateFullname(viewFragment.editTextFullname.text.toString())
    }

    // Change Fragment
    private fun loadEmailFragment() {
        val transaction: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.layoutAuthenticationFragmentPlace, EmailFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun loadSignInFragment() {
        val transaction: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.layoutAuthenticationFragmentPlace, SigninFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }


}