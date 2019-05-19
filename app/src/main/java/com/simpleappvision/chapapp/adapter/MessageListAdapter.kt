package com.simpleappvision.chapapp.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.simpleappvision.chapapp.R
import com.simpleappvision.chapapp.model.Message
import com.simpleappvision.chapapp.utils.PicassoHelper
import java.util.*


class MessageListAdapter(
    var context: Context,
    var list: ArrayList<Message>,
    var onItemClick: MessageListAdapter.OnItemClick
) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = inflater.inflate(R.layout.messages_list_item_sender, parent, false) // default
        var imageviewCheck: ImageView = view.findViewById(R.id.imageViewCheck) as ImageView // default
        var textViewDate = view.findViewById(R.id.textViewDate) as TextView

        if (list[position].getSender() == FirebaseAuth.getInstance().uid) {

            if (list[position].getType() == "text") {

                var textView = view.findViewById(R.id.textViewMessage) as TextView
                textView.text = list[position].getText()
                textViewDate.text = list[position].getDate()

            } else if (list[position].getType() == "image") {
                view = inflater.inflate(R.layout.messages_list_item_sender_photo, parent, false)
                imageviewCheck = view.findViewById(R.id.imageViewCheck) as ImageView
                textViewDate = view.findViewById(R.id.textViewDate) as TextView
                var photo = view.findViewById(R.id.imageViewPhoto) as ImageView
                var picassoHelper = PicassoHelper(photo)
                picassoHelper.getPhoto(list[position].getUrl())
                textViewDate.text = list[position].getDate()

            }else{
                // video
                view = inflater.inflate(R.layout.message_list_item_sender_video, parent, false)
                imageviewCheck = view.findViewById(R.id.imageViewCheck) as ImageView
                textViewDate = view.findViewById(R.id.textViewDate) as TextView
                textViewDate.text = list[position].getDate()

            }
            // Check IF Receiver Got Message
            if (list[position].getStatus() == "1") {
                imageviewCheck.setImageDrawable(context.getDrawable(R.drawable.ic_green_check))
            }

        } else {


            if (list[position].getType() == "text") {

                view = inflater.inflate(R.layout.message_list_item_receiver, parent, false)
                var textViewDate = view.findViewById(R.id.textViewDate) as TextView
                var textView = view.findViewById(R.id.textViewMessage) as TextView
                textView.text = list[position].getText()
                textViewDate.text = list[position].getDate()

            } else if (list[position].getType() == "image") {

                view = inflater.inflate(R.layout.messages_list_item_receiver_photo, parent, false)
                var textViewDate = view.findViewById(R.id.textViewDate) as TextView
                var photo = view.findViewById(R.id.imageViewPhoto) as ImageView
                var picassoHelper = PicassoHelper(photo)
                picassoHelper.getPhoto(list[position].getUrl())
                textViewDate.text = list[position].getDate()

            } else{
                //video
                view = inflater.inflate(R.layout.message_list_item_receiver_video, parent, false)
            }


        }

        view.setOnClickListener {
            onItemClick.onItemClick(list[position].getUrl(),list[position].getType())
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return list.size
    }


    fun getAdapterList(): ArrayList<Message> {
        return list
    }

    interface OnItemClick {
        fun onItemClick(url: String,type:String) {}
    }

}