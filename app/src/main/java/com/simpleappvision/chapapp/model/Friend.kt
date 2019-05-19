package com.simpleappvision.chapapp.model

class Friend {


    private lateinit var email:String
    private lateinit var fullname:String
    private lateinit var url:String



    constructor(email:String,fullname:String,url:String){
        this.email = email
        this.fullname = fullname
        this.url = url
    }
    constructor(){}

    fun setEmail(email: String){
        this.email = email
    }

    fun setFullname(fullname: String){
        this.fullname = fullname
    }

    fun setUrl(url: String){
        this.url = url
    }


    fun getEmail():String{
        return this.email
    }

    fun getFullname():String{
        return this.fullname
    }

    fun getUrl():String{
        return this.url
    }

}