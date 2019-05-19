package com.simpleappvision.chapapp.home

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.simpleappvision.chapapp.R
import kotlinx.android.synthetic.main.activity_add_friend.*


class AddFriendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)
        buttonAdd.setOnClickListener {

            if (isEmailValid()) {
                addFriend()
            } else {
                Toast.makeText(this, "Geçerli Bir Email Adresi Girin.", Toast.LENGTH_LONG).show()
            }

        }

        imageViewBack.setOnClickListener {
            startHomeActivity()
        }
        textViewBack.setOnClickListener {
            startHomeActivity()
        }


    }


    private fun isEmailValid(): Boolean {
        if (editTextEmail.text.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(editTextEmail.text).matches()) {
            return true
        }
        return false
    }


    private fun addFriend() {
        FirebaseDatabase.getInstance().reference.child("users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var isFind = false
                    for (dataSnapShot in p0.children) {
                        if (editTextEmail.text.toString() == dataSnapShot.child("profile").child("email").getValue(
                                String::class.java
                            )
                        ) {
                            var map = HashMap<String, String>()
                            map["uid"] = dataSnapShot.key!!
                            FirebaseDatabase.getInstance().reference.child("users")
                                .child(FirebaseAuth.getInstance().uid!!).child("friends").push().setValue(map)
                            map["uid"] = FirebaseAuth.getInstance().uid!!
                            FirebaseDatabase.getInstance().reference.child("users").child(dataSnapShot.key!!)
                                .child("friends").push().setValue(map)
                            isFind = true
                        }
                    }

                    if (isFind) {
                        Toast.makeText(this@AddFriendActivity, "Kişi Arkadaş Olarak Eklendi", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@AddFriendActivity, "Kişi Bulunamadı", Toast.LENGTH_LONG).show()
                    }

                }


            })
    }


    private fun startHomeActivity() {
        var intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        startHomeActivity()
    }


}
