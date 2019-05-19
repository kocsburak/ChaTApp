package com.simpleappvision.chapapp.model

class Message {


    private lateinit var sender: String
    private lateinit var receiver: String
    private lateinit var text: String
    private lateinit var date: String
    private lateinit var status: String
    private lateinit var type: String
    private lateinit var url: String
    private var count: Int = 0

    constructor() {}
    constructor(
        sender: String,
        receiver: String?,
        text: String,
        date: String,
        status: String,
        count: Int,
        type: String,
        url: String
    ) {
        this.sender = sender
        this.receiver = receiver!!
        this.text = text
        this.date = date
        this.status = status
        this.count = count
        this.type = type
        this.url = url
    }


    fun setSender(sender: String) {
        this.sender = sender
    }

    fun setReceiver(receiver: String?) {
        this.receiver = receiver!!
    }

    fun setText(text: String) {
        this.text = text
    }

    fun setDate(date: String) {
        this.date = date
    }

    fun setStatus(status: String) {
        this.status = status
    }

    fun setCount(count: Int) {
        this.count = count
    }

    fun setType(type: String) {
        this.type = type
    }

    fun setUrl(url: String) {
        this.url = url
    }

    fun getSender(): String {
        return this.sender
    }

    fun getReceiver(): String? {
        return this.receiver
    }

    fun getText(): String {
        return this.text
    }

    fun getDate(): String {
        return this.date
    }

    fun getStatus(): String {
        return this.status
    }

    fun getCount(): Int {
        return this.count
    }

    fun getType(): String {
        return this.type
    }

    fun getUrl(): String {
        return this.url
    }

}