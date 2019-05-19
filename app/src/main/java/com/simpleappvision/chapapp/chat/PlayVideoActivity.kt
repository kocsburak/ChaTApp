package com.simpleappvision.chapapp.chat

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.simpleappvision.chapapp.R
import com.simpleappvision.chapapp.utils.EventBusDataSender
import kotlinx.android.synthetic.main.activity_play_video.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class PlayVideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)
        imageViewBack.setOnClickListener {
            this.finish()
        }
    }


    @Subscribe(sticky = true)
    internal fun onUserEvents(data: EventBusDataSender.sendVideoUrl) {
        downloadVideo(data.url)
        Log.e("Hata:",data.url)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    private fun downloadVideo(url: String) {
        var uri = Uri.parse(url)
        videoView.setVideoURI(uri)
        videoView.requestFocus()
        videoView.start()
    }


}
