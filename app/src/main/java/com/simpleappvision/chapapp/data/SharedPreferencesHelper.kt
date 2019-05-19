package com.simpleappvision.chapapp.data

import android.content.Context
import android.content.SharedPreferences


class SharedPreferencesHelper {

    private var context: Context
    private var preferences : SharedPreferences
    private var editor: SharedPreferences.Editor

    constructor(context: Context) {
        this.context = context
        preferences = context.getSharedPreferences("Session", 0) // 0 - for private mode
        editor = preferences.edit()
    }


    fun updateFullname(fullname:String){
        editor.putString("fullname",fullname)
        editor.commit()
    }

    fun updateEmail(email:String){
        editor.putString("email",email)
        editor.commit()
    }

    fun updatePhotoUrl(url:String){
        editor.putString("url",url)
        editor.commit()
    }

    fun getLastUser(): HashMap<String, String> {

        var map = HashMap<String, String>()
        map["fullname"] = preferences.getString("fullname","")
        map["email"] = preferences.getString("email","")
        map["url"] = preferences.getString("url","")

        return map
    }

}