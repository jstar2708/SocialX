package com.example.news.datamodels

class User {
    private lateinit var userName: String
    private lateinit var password: String
    private lateinit var email: String
    private lateinit var userId: String
    private lateinit var phoneNumber: String

    constructor(){}

    constructor(userName: String, password: String, email: String, userId: String, phoneNumber: String){
        this.email = email
        this.userId = userId
        this.userName = userName
        this.password = password
        this.phoneNumber = phoneNumber
    }

    constructor(email: String, userName: String, password: String){
        this.userName = userName
        this.email = email
        this.password = password
    }

    fun getEmail(): String{
        return this.email
    }

    fun setEmail(email: String){
        this.email = email
    }


    fun getPassword(): String{
        return this.password
    }

    fun setPassword(password: String){
        this.password = password
    }


    fun getUserId(): String{
        return this.userId
    }

    fun setUserId(userId: String){
        this.userId = userId
    }

    fun getUserName(): String{
        return this.userName
    }

    fun setUserName(userName: String){
        this.userName = userName
    }

    fun getPhoneNumber(): String{
        return this.phoneNumber
    }

    fun setPhoneNumber(phoneNumber: String){
        this.phoneNumber = phoneNumber
    }
}