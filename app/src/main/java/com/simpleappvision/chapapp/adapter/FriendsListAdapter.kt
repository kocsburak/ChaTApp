package com.simpleappvision.chapapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.simpleappvision.chapapp.R

import com.simpleappvision.chapapp.model.User
import com.simpleappvision.chapapp.utils.PicassoHelper


class FriendsListAdapter(var context: Context, var list: ArrayList<User>,var onItemClick: OnItemClick) : BaseAdapter() {


    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var picassoHelper : PicassoHelper



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = inflater.inflate(R.layout.friends_list_item,parent,false)

        val name = view.findViewById(R.id.textViewName) as TextView
        val email = view.findViewById(R.id.textViewEmail) as TextView
        val imageView  = view.findViewById(R.id.imageViewProfilePhoto) as ImageView
        picassoHelper = PicassoHelper(imageView)



        name.text = list[position].getFullname()
        email.text = list[position].getEmail()
        picassoHelper.getPhoto(list[position].getUrl())

        view.setOnClickListener {
        onItemClick.onItemClick(position)
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


    interface OnItemClick{
        fun onItemClick(position: Int){}
    }



}