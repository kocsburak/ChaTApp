package com.simpleappvision.chapapp.chat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.simpleappvision.chapapp.R
import com.simpleappvision.chapapp.adapter.MessageListAdapter
import com.simpleappvision.chapapp.home.HomeActivity
import com.simpleappvision.chapapp.model.Message
import com.simpleappvision.chapapp.utils.EventBusDataSender
import com.simpleappvision.chapapp.utils.PicassoHelper
import kotlinx.android.synthetic.main.activity_chat.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.IOException


import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatActivity : AppCompatActivity(), View.OnClickListener, MessageListAdapter.OnItemClick {

    private lateinit var uid: String
    private var pushId: String = "0"
    private lateinit var messages: ArrayList<Message>
    private lateinit var adapter: MessageListAdapter
    private var messageCount = -1
    private var filePath: Uri? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        imageViewSend.setOnClickListener(this)
        imageViewBack.setOnClickListener(this)
        imageViewPhoto.setOnClickListener(this)
        imageViewVideo.setOnClickListener(this)

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        reference = FirebaseDatabase.getInstance().reference
        setupAdapter()
        showMessages()
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.imageViewSend -> {
                if (isMessageValid()) {
                    sendMessage("text", "")
                    editText.text = null
                }
            }
            R.id.imageViewBack -> {
                startHomeActivity()
            }
            R.id.imageViewPhoto -> {
                openGallery("image")
            }
            R.id.imageViewVideo -> {
                openGallery("video")
            }

        }
    }

    @Subscribe(sticky = true)
    internal fun onUserEvents(data: EventBusDataSender.sendFullnameAndPhoto) {
        textViewFullname.text = data.fullname
        uid = data.uid
        getProfilePhoto(data.photoUrl)
        Log.e("Uid", "" + uid)
        getMessages()
    }

    private fun getProfilePhoto(url: String) {
        var picassoHelper = PicassoHelper(imageViewProfilePhoto)
        picassoHelper.getPhoto(url)
    }

    private fun setupAdapter() {
        messages = ArrayList()
        adapter = MessageListAdapter(this, messages, this)
        listViewMessage.adapter = adapter
    }

    private fun getMessages() {
        FirebaseDatabase.getInstance().reference.child("messages").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChildren()) {
                    for (dataSnapShot in p0.children) {
                        var user1 = dataSnapShot.child("users").child("uid1").getValue(String::class.java)
                        var user2: String? = dataSnapShot.child("users").child("uid2").getValue(String::class.java)

                        if ((user1 == FirebaseAuth.getInstance().uid && user2 == uid) || (user1 == uid && user2 == FirebaseAuth.getInstance().uid)) {
                            pushId = dataSnapShot.key!!
                            if (dataSnapShot.hasChild("messages")) {
                                for (messageSnapShot in dataSnapShot.child("messages").children) {
                                    var message = messageSnapShot.getValue(Message::class.java)
                                    if (message!!.getCount() > messageCount) {
                                        if (message.getReceiver() == FirebaseAuth.getInstance().uid) {
                                            FirebaseDatabase.getInstance().reference.child("messages")
                                                .child(dataSnapShot.key!!).child("messages")
                                                .child(messageSnapShot.key!!).child("status").setValue("1")
                                        }
                                        messages.add(message!!)
                                        messageCount = message!!.getCount()
                                    }

                                    if (message.getSender() == FirebaseAuth.getInstance().uid!! && message.getStatus() == "1") {
                                        var temp: Message = adapter.getAdapterList()[message.getCount()]
                                        temp.setStatus("1")
                                    }


                                }

                                if (messages.size > 0) {
                                    showMessages()
                                }
                            }
                        }

                    }
                }
            }


        })
    }

    private fun showMessages() {
        adapter.notifyDataSetChanged()
    }

    private fun isMessageValid(): Boolean {

        if (!editText.text.isEmpty()) {
            return true
        }
        return false
    }

    private fun sendMessage(type: String, url: String) {

        if (messages.size == 0) {
            var map = HashMap<String, String>()
            map.put("uid1", FirebaseAuth.getInstance().uid!!)
            map.put("uid2", uid)

            updateKey()

            reference.child("messages").child(pushId).child("users").setValue(map)
        }
        if (type == "text") {
            messageCount++
        }
        var message = Message(
            FirebaseAuth.getInstance().uid!!,
            uid,
            editText.text.toString(),
            getCurrentTime(),
            "0",
            messageCount,
            type,
            url
        )
        FirebaseDatabase.getInstance().reference.child("messages").child(pushId).child("messages").push()
            .setValue(message)
        messages.add(message)
        showMessages()
    }

    fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val mdformat = SimpleDateFormat("HH:mm:ss")
        val strDate = mdformat.format(calendar.time)
        return strDate
    }

    private fun openGallery(type: String) {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        if (type == "image") {
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Choose Photo"), 1)
        } else {
            intent.type = "video/*"
            intent.action = Intent.ACTION_GET_CONTENT;
            startActivityForResult(Intent.createChooser(intent, "Choose Video"), 2)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            filePath = data.data
            Log.e("Hata:", "Error1")
            try {

                var type = "image"
                if(requestCode == 2){
                    type = "video"
                }

                uploadMedia(type)
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("Hata:", "Error")
            }

        }
    }

    private fun uploadMedia(type:String) {
        messageCount++
        updateKey()
        storageReference.child("messages").child(pushId).child("" + messageCount).putFile(filePath!!)
            .addOnCompleteListener { p0 ->
                if (p0.isSuccessful) {
                    getDowloandUrl(type)
                } else {
                    Toast.makeText(this@ChatActivity, "Something Went Wrong!", Toast.LENGTH_LONG).show()
                }
            }
    }


    private fun getDowloandUrl(type:String) {
        storageReference.child("messages").child(pushId).child("" + messageCount)
            .downloadUrl.addOnCompleteListener { p0 ->

            if (p0.isSuccessful) {
                sendMessage(type, p0!!.result.toString())
            }

        }
    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun startHomeActivity() {
        var intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        this.finish()
    }


    private fun updateKey() {
        if (pushId == "0") {
            pushId = reference.push().key!!
        }
    }


    override fun onItemClick(url: String,type:String) {
        if(type == "image"){
            EventBus.getDefault().postSticky(EventBusDataSender.sendPhotoUrl(url))
            startShowPhotoActivity()
        }else{
            EventBus.getDefault().postSticky(EventBusDataSender.sendVideoUrl(url))
            startPlayVideoActivity()
        }
    }

    private fun startShowPhotoActivity() {
        var intent = Intent(this, ShowPhotoActivity::class.java)
        startActivity(intent)
    }

    private fun startPlayVideoActivity(){
        var intent = Intent(this, PlayVideoActivity::class.java)
        startActivity(intent)
    }

}
