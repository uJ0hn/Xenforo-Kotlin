package com.muhdev.xenforo.interfaces

interface User {

    fun userExists() : Boolean
    fun getId() : Int
    fun getName() : String

    fun changePassword(newpass : String)


}