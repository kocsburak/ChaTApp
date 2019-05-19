package com.simpleappvision.chapapp.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.simpleappvision.chapapp.R
import com.simpleappvision.chapapp.model.User
import com.simpleappvision.chapapp.utils.PicassoHelper
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.IOException


class ProfileActivity : AppCompatActivity() {


    private var filePath: Uri? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var picassoHelper: PicassoHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupVariables()
        setClicks()
        getProfileInformation()

    }

    private fun setupVariables() {
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
    }

    private fun setClicks() {
        // Back
        imageViewBack.setOnClickListener {
            startHomeActivity()
        }

        textViewBack.setOnClickListener {
            startHomeActivity()
        }

        // Done
        buttonDone.setOnClickListener {
            if (isFullnameValid()) {
                updateFullname()
            }
            if (filePath != null) {
                uploadProfilePhoto()
            }
            if (isEmailValid()) {
                updateEmail()
            }
            if (isPasswordValid()) {
                updatePassword()
            }
            startHomeActivity()
        }

        textViewChangePhoto.setOnClickListener {
            openGallery()
        }
    }

    private fun isFullnameValid(): Boolean {
        if (editTextFullname.text.toString().isNotEmpty()) {
            return true
        }
        return false
    }

    private fun updateFullname() {
        FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().uid!!).child("profile")
            .child("fullname").setValue(editTextFullname.text.toString())
    }

    private fun isEmailValid(): Boolean {
        if (editTextEmail.text.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(editTextEmail.text).matches()) {
            return true
        }
        return false
    }

    private fun updateEmail() {
        FirebaseAuth.getInstance().currentUser!!.updateEmail(editTextEmail.text.toString())
        FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().uid!!).child("profile")
            .child("email").setValue(editTextEmail.text.toString())
    }

    private fun isPasswordValid(): Boolean {
        if (editTextPassword.text.isNotEmpty() && editTextPassword.text.length > 5) {
            return true
        }
        return false
    }

    private fun updatePassword() {
        FirebaseAuth.getInstance().currentUser!!.updatePassword(editTextPassword.text.toString())
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Choose Your Profile Photo"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 1 && resultCode === Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageViewProfilePhoto.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private fun uploadProfilePhoto() {

        storageReference.child("photos").child(FirebaseAuth.getInstance().uid!!).child("profile").putFile(filePath!!)
            .addOnCompleteListener { p0 ->
                if (p0.isSuccessful) {
                    updateUserPhotoUrl()
                } else {
                    Toast.makeText(this@ProfileActivity, "Something Went Wrong!", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun updateUserPhotoUrl() {
        storageReference.child("photos").child(FirebaseAuth.getInstance().uid!!).child("profile")
            .downloadUrl.addOnCompleteListener { p0 ->

            if (p0.isSuccessful) {
                FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().uid!!)
                    .child("profile").child("url").setValue(p0.result!!.toString())
            }

        }
    }

    private fun getProfileInformation() {
        FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().uid!!).child("profile")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var user = p0.getValue(User::class.java)
                    setInformations(user!!)
                }

            })
    }

    private fun setInformations(user: User) {
        editTextFullname.setText(user.getFullname())
        editTextEmail.setText(user.getEmail())
        picassoHelper = PicassoHelper(imageViewProfilePhoto)
        picassoHelper.getPhoto(user.getUrl())
    }

    private fun startHomeActivity() {
        var intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        this.finish()
    }


}
// TODO : Bilgileri ve Profil Photo Çek
// TODO : Eski Fotoğraf oldugunda fotografı upload etmemesi lazım algoyu yaz