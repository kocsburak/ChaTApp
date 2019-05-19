package com.simpleappvision.chapapp.authentication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.simpleappvision.chapapp.R
import com.simpleappvision.chapapp.data.SharedPreferencesHelper
import com.simpleappvision.chapapp.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_signup_password.view.*

class PasswordFragment : Fragment(), View.OnClickListener, TextWatcher {


    private lateinit var viewFragment: View
    private lateinit var helper: SharedPreferencesHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewFragment = inflater.inflate(R.layout.fragment_signup_password, container, false)
        viewFragment.editTextPassword.addTextChangedListener(this)
        viewFragment.buttonDone.setOnClickListener(this)

        helper = SharedPreferencesHelper(activity!!)
        return viewFragment
    }


    override fun onClick(v: View?) {
        setDisableEditText()
        setDisableDoneButton()
        showProgressBar()
        signUp()
    }

    override fun afterTextChanged(s: Editable?) {
        // Do Nothing ..
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Do Nothing ..
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (isPasswordValid()) {
            setEnableDoneButton()
        } else {
            setDisableDoneButton()
        }
    }


    private fun isPasswordValid(): Boolean {
        if (viewFragment.editTextPassword.text.length > 5) {
            return true
        }
        return false
    }


    //
    private fun setEnableDoneButton() {
        viewFragment.buttonDone.isEnabled = true
        viewFragment.buttonDone.setTextColor(ContextCompat.getColor(context!!, R.color.colorAccent))
        viewFragment.buttonDone.alpha = 1F
    }

    private fun setDisableDoneButton() {
        viewFragment.buttonDone.isEnabled = false
        viewFragment.buttonDone.setTextColor(ContextCompat.getColor(context!!, R.color.colorText))
        viewFragment.buttonDone.alpha = 0.3F
    }


    // Progress Bar Area
    private fun showProgressBar() {
        viewFragment.groupProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        viewFragment.groupProgressBar.visibility = View.GONE
    }


    // Other Objects Must Be Disable During Progress Bar is Showing
    private fun setDisableEditText() {
        viewFragment.editTextPassword.isEnabled = false
    }

    private fun setEnableEditText() {
        viewFragment.editTextPassword.isEnabled = true
    }


    private fun signUp() {
        // Hasmap contain fullname and email
        var map: HashMap<String, String> = helper.getLastUser()

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(map["email"]!!, viewFragment.editTextPassword.text.toString())
            .addOnCompleteListener { p0 ->
                if (p0!!.isSuccessful) {
                    FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().uid!!)
                        .child("profile").setValue(map)
                    startHomeActivity()
                } else {
                    setEnableEditText()
                    setEnableDoneButton()
                    hideProgressBar()
                    Toast.makeText(activity!!, getString(R.string.error_went_wrong), Toast.LENGTH_LONG).show()
                }
            }
    }


    private fun startHomeActivity() {
        val intent = Intent(activity!!, HomeActivity::class.java)
        startActivity(intent)
        activity!!.finish()
    }


}

// TODO: NOTE -> HOME EKRANINDA KİŞİNİN VERİLERİNİ ÇEKECEĞİZ EGER VERİLER YOKSA KİŞİ KAYIT OLURKEN ERROR ALMIŞTIR KİŞİYİ SİL KAYIT EKRANINA YÖNLENDİR