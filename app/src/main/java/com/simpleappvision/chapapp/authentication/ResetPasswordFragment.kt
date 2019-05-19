package com.simpleappvision.chapapp.authentication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import kotlinx.android.synthetic.main.fragment_reset_password.view.*

class ResetPasswordFragment : Fragment(), View.OnClickListener, TextWatcher {


    private lateinit var viewFragment: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewFragment = inflater.inflate(R.layout.fragment_reset_password, container, false)
        viewFragment.buttonSend.setOnClickListener(this)
        viewFragment.textViewReturnToSignIn.setOnClickListener(this)

        viewFragment.editTextEmail.addTextChangedListener(this)
        return viewFragment
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.buttonSend -> {
                showProgressBar()
                disableScreen()
                sendNewPasswordRequest()
            }
            R.id.textViewReturnToSignIn -> {
                loadSignInFragment()
            }
        }
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


    //
    private fun setEnableNextButton() {
        viewFragment.buttonSend.isEnabled = true
        viewFragment.buttonSend.setTextColor(ContextCompat.getColor(context!!, R.color.colorAccent))
        viewFragment.buttonSend.alpha = 1F
    }

    private fun setDisableNextButton() {
        viewFragment.buttonSend.isEnabled = false
        viewFragment.buttonSend.setTextColor(ContextCompat.getColor(context!!, R.color.colorText))
        viewFragment.buttonSend.alpha = 0.3F
    }


    private fun sendNewPasswordRequest() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(viewFragment.editTextEmail.text.toString())
            .addOnCompleteListener { p0 ->
                if (p0.isSuccessful) {
                    Toast.makeText(activity!!, getString(R.string.text_password_request_success), Toast.LENGTH_LONG).show()
                    loadSignInFragment()
                } else {
                    hideProgressBar()
                    enableScreen()
                    Toast.makeText(activity!!, getString(R.string.error_password_request), Toast.LENGTH_LONG).show()
                }
            }
    }


    // Progress Bar Area
    private fun showProgressBar() {
        viewFragment.groupResetPassword.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        viewFragment.groupResetPassword.visibility = View.GONE
    }


    // Other Objects Must Be Disable During Progress Bar is Showing
    private fun disableScreen() {
        viewFragment.editTextEmail.isEnabled = false
        viewFragment.buttonSend.isEnabled = false

    }

    private fun enableScreen() {
        viewFragment.editTextEmail.isEnabled = true
        viewFragment.buttonSend.isEnabled = true
    }

    private fun loadSignInFragment() {
        val transaction: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.layoutAuthenticationFragmentPlace, SigninFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

}

// TODO : Buttonu Aktif Etmeyi 2 farklı Fonksiyondada Kullanmışız Düzelt (Sign In Dede Aynı Warning Var)
// TODO : Fragment leri BackStack a Dönecek Şekilde Çagırmak Lazım