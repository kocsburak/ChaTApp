package com.simpleappvision.chapapp.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.simpleappvision.chapapp.R
import com.simpleappvision.chapapp.utils.EventBusDataSender
import com.simpleappvision.chapapp.utils.PicassoHelper
import kotlinx.android.synthetic.main.activity_show_photo.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ShowPhotoActivity : AppCompatActivity() {

    private lateinit var picassoHelper:PicassoHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_photo)
        picassoHelper = PicassoHelper(imageViewPhoto)

        imageViewBack.setOnClickListener {
            this.finish()
        }

    }

    @Subscribe(sticky = true)
    internal fun onUserEvents(data: EventBusDataSender.sendPhotoUrl) {
        picassoHelper.getPhoto(data.url)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

}
