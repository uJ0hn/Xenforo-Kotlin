package com.muhdev.xenforo.interfaces

interface Group {

    fun getName() : String
    fun getId() : Int
    fun getUsersWithPrimary() : List<User>
    fun getUsersWithSecundary() : List<User>

    fun groupExists() : Boolean

}