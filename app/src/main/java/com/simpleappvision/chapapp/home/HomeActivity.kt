package com.simpleappvision.chapapp.home

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.simpleappvision.chapapp.MainActivity
import com.simpleappvision.chapapp.R
import com.simpleappvision.chapapp.adapter.FriendsListAdapter
import com.simpleappvision.chapapp.chat.ChatActivity
import com.simpleappvision.chapapp.model.User
import com.simpleappvision.chapapp.utils.EventBusDataSender
import kotlinx.android.synthetic.main.activity_home.*
import org.greenrobot.eventbus.EventBus

class HomeActivity : AppCompatActivity(), View.OnClickListener, FriendsListAdapter.OnItemClick {


    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var friendsList: ArrayList<User>
    private lateinit var uids:ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupVariables()
        getFriendsUid()

    }


    private fun setupVariables() {
        mAuth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
        friendsList = ArrayList<User>()
        uids = ArrayList<String>()
        //
        imageViewAdd.setOnClickListener(this)
        imageViewProfile.setOnClickListener(this)
        textViewSignOut.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.imageViewAdd -> {
                startAddFriendActivity()
            }
            R.id.imageViewProfile -> {
                startProfileActivity()
            }
            R.id.textViewSignOut -> {
                signOut()
                startMainActivity()
            }
        }
    }


    private fun getFriendsUid() {
        databaseRef.child("users").child("" + mAuth.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.hasChild("friends")) {

                        for (snapShot in p0.child("friends").children) {
                            uids.add(snapShot.child("uid").getValue(String::class.java)!!)
                        }
                        getFriends(uids)
                    } else {
                        showNoFriendsScreen()
                    }
                }


            })

    }


    private fun getFriends(uids: ArrayList<String>) {
        var sayac = 0
        databaseRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (snapShot in p0.children) {

                    var i = 0
                    while (i < uids.size) {
                        if (snapShot.key == uids[i]) {
                            var friend = snapShot.child("profile").getValue(User::class.java)
                            friend!!.uid = uids[i]
                            friendsList.add(friend!!)
                            sayac++
                            break
                        }
                        i++
                    }
                }

                if (sayac > 0) {
                    showFriendList()
                    showFriendListScreen()
                } else {
                    showNoFriendsScreen()
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        })
    }


    private fun showNoFriendsScreen() {
        groupListView.visibility = View.GONE
        groupProgress.visibility = View.GONE
        groupNoFriends.visibility = View.VISIBLE
    }

    private fun showFriendListScreen() {
        groupProgress.visibility = View.GONE
        groupNoFriends.visibility = View.GONE
        groupListView.visibility = View.VISIBLE
    }

    private fun showFriendList() {
        var adapter = FriendsListAdapter(this, friendsList, this)
        listViewFriends.adapter = adapter
    }


    private fun startAddFriendActivity() {
        var intent = Intent(this, AddFriendActivity::class.java)
        startActivity(intent)
        this.finish()
    }


    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    private fun startMainActivity() {
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun startProfileActivity() {
        var intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        this.finish()
    }


    override fun onItemClick(position: Int) {
        EventBus.getDefault().postSticky(EventBusDataSender.sendFullnameAndPhoto(friendsList[position].getFullname(),friendsList[position].getUrl(),friendsList[position].uid))
        startChatActivity()
    }

    private fun startChatActivity(){
        var intent = Intent(this, ChatActivity::class.java)
        startActivity(intent)
        this.finish()
    }

}
